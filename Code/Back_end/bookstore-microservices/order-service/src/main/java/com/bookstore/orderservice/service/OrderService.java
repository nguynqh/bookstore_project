package com.bookstore.orderservice.service;

import com.bookstore.orderservice.dto.BookDTO;
import com.bookstore.orderservice.dto.CustomerDTO;
import com.bookstore.orderservice.dto.OrderDTO;
import com.bookstore.orderservice.dto.OrderItemDTO;
import com.bookstore.orderservice.dto.OrderRequest;
import com.bookstore.orderservice.entity.Order;
import com.bookstore.orderservice.entity.OrderItem;
import com.bookstore.orderservice.entity.OrderStatus;
import com.bookstore.orderservice.event.OrderCreatedEvent;
import com.bookstore.orderservice.event.OrderStatusChangedEvent;
import com.bookstore.orderservice.exception.InsufficientStockException;
import com.bookstore.orderservice.exception.OrderNotFoundException;
import com.bookstore.orderservice.repository.OrderRepository;
import com.bookstore.orderservice.service.client.BookServiceClient;
import com.bookstore.orderservice.service.client.CustomerServiceClient;
import com.bookstore.orderservice.service.client.InventoryServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookServiceClient bookServiceClient;
    private final CustomerServiceClient customerServiceClient;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ModelMapper modelMapper;


    @Value("${spring.kafka.topic.order-created}")
    private String ORDER_CREATED_TOPIC;

    @Value("${spring.kafka.topic.order-status-changed}")
    private String ORDER_STATUS_CHANGED_TOPIC;

    public OrderService(
            OrderRepository orderRepository,
            @Lazy BookServiceClient bookServiceClient,
            @Lazy CustomerServiceClient customerServiceClient,
            @Lazy InventoryServiceClient inventoryServiceClient,
            KafkaTemplate<String, Object> kafkaTemplate,
            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.bookServiceClient = bookServiceClient;
        this.customerServiceClient = customerServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
    }

    public Page<OrderDTO> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::convertToOrderDTO);
    }

    public OrderDTO findById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return convertToOrderDTO(order);
    }

    public List<OrderDTO> findByCustomerId(UUID customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO createOrder(OrderRequest orderRequest) {
        // Validate stock availability
        validateStockAvailability(orderRequest);

        // Get customer details
        CustomerDTO customer = customerServiceClient.getCustomerById(orderRequest.getCustomerId());

        // Create order entity
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setCustomerId(orderRequest.getCustomerId());
        order.setEmployeeId(orderRequest.getEmployeeId());
        order.setShippingAddress(orderRequest.getShippingAddress() != null ?
                orderRequest.getShippingAddress() : customer.getAddress());
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setOrderNotes(orderRequest.getOrderNotes());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        // Create order items
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var itemRequest : orderRequest.getItems()) {
            BookDTO book = bookServiceClient.getBookById(itemRequest.getBookId());

            OrderItem item = OrderItem.builder()
                    .bookId(itemRequest.getBookId())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(itemRequest.getUnitPrice())
                    .discount(itemRequest.getDiscount() != null ? itemRequest.getDiscount() : BigDecimal.ZERO)
                    .build();

            order.addOrderItem(item);
            totalAmount = totalAmount.add(item.getSubtotal());
        }

        order.setTotalAmount(totalAmount);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Publish event
        publishOrderCreatedEvent(savedOrder);

        return convertToOrderDTO(savedOrder);
    }

    @Transactional
    public OrderDTO updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);

        Order updatedOrder = orderRepository.save(order);

        // Publish status changed event
        publishOrderStatusChangedEvent(updatedOrder, oldStatus, newStatus);

        return convertToOrderDTO(updatedOrder);
    }

    @Transactional
    public OrderDTO cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Cannot cancel an order that has been shipped or delivered");
        }

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.CANCELLED);

        Order updatedOrder = orderRepository.save(order);

        // Publish status changed event
        publishOrderStatusChangedEvent(updatedOrder, oldStatus, OrderStatus.CANCELLED);

        return convertToOrderDTO(updatedOrder);
    }

    private void validateStockAvailability(OrderRequest orderRequest) {
        for (var item : orderRequest.getItems()) {
            boolean isAvailable = inventoryServiceClient.checkStock(item.getBookId(), item.getQuantity());

            if (!isAvailable) {
                BookDTO book = bookServiceClient.getBookById(item.getBookId());
                throw new InsufficientStockException("Insufficient stock for book: " + book.getTitle());
            }
        }
    }

    private OrderDTO convertToOrderDTO(Order order) {
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

        // Enhance with more details if needed
        try {
            CustomerDTO customer = customerServiceClient.getCustomerById(order.getCustomerId());
            orderDTO.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
        } catch (Exception e) {
            log.warn("Could not fetch customer details for order {}: {}", order.getId(), e.getMessage());
        }

        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> {
                    OrderItemDTO itemDTO = modelMapper.map(item, OrderItemDTO.class);
                    try {
                        BookDTO book = bookServiceClient.getBookById(item.getBookId());
                        itemDTO.setBookTitle(book.getTitle());
                    } catch (Exception e) {
                        log.warn("Could not fetch book details for item {}: {}", item.getId(), e.getMessage());
                        itemDTO.setBookTitle("Unknown Book");
                    }
                    return itemDTO;
                })
                .collect(Collectors.toList());

        orderDTO.setItems(itemDTOs);
        return orderDTO;
    }

    private void publishOrderCreatedEvent(Order order) {
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(order.getId())
                .customerId(order.getCustomerId())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .paymentMethod(order.getPaymentMethod())
                .items(order.getItems().stream()
                        .map(item -> OrderCreatedEvent.OrderItemEvent.builder()
                                .bookId(item.getBookId())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        try {
            kafkaTemplate.send(ORDER_CREATED_TOPIC, order.getId().toString(), event);
            log.info("Published order created event for order: {}", order.getId());
        } catch (Exception e) {
            log.error("Failed to publish order created event for order: {}", order.getId(), e);
        }
    }

    private void publishOrderStatusChangedEvent(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        OrderStatusChangedEvent event = OrderStatusChangedEvent.builder()
                .orderId(order.getId())
                .customerId(order.getCustomerId())
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .timestamp(LocalDateTime.now())
                .build();

        try {
            kafkaTemplate.send(ORDER_STATUS_CHANGED_TOPIC, order.getId().toString(), event);
            log.info("Published order status changed event for order: {}", order.getId());
        } catch (Exception e) {
            log.error("Failed to publish order status changed event for order: {}", order.getId(), e);
        }
    }
}