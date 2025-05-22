package com.bookstore.paymentservice.service;

import com.bookstore.paymentservice.dto.OrderDTO;
import com.bookstore.paymentservice.dto.PaymentDTO;
import com.bookstore.paymentservice.dto.PaymentRequest;
import com.bookstore.paymentservice.dto.RefundRequest;
import com.bookstore.paymentservice.entity.Payment;
import com.bookstore.paymentservice.entity.Refund;
import com.bookstore.paymentservice.event.PaymentEvent;
import com.bookstore.paymentservice.exception.PaymentException;
import com.bookstore.paymentservice.repository.PaymentRepository;
import com.bookstore.paymentservice.repository.RefundRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RestTemplate restTemplate;

    @Value("${service.order-service.url}")
    private String orderServiceUrl;

    @Value("${kafka.topics.payment-completed}")
    private String paymentCompletedTopic;

    @Autowired
    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            RefundRepository refundRepository,
            ModelMapper modelMapper,
            KafkaTemplate<String, Object> kafkaTemplate,
            RestTemplate restTemplate) {
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
        this.modelMapper = modelMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public PaymentDTO processPayment(PaymentRequest paymentRequest) {
        log.info("Processing payment for order: {}", paymentRequest.getOrderId());

        // Check if order exists and get order details
        OrderDTO orderDTO = getOrderDetails(paymentRequest.getOrderId());

        if (orderDTO == null) {
            throw new PaymentException("Order not found: " + paymentRequest.getOrderId());
        }

        // Validate payment amount against order amount
        if (paymentRequest.getAmount().compareTo(orderDTO.getTotalAmount()) != 0) {
            throw new PaymentException("Payment amount does not match order amount");
        }

        // Process payment (in a real system, this would integrate with a payment gateway)
        Payment payment = processPaymentWithGateway(paymentRequest);

        // Save payment record
        payment = paymentRepository.save(payment);

        // Notify order service about successful payment
        PaymentEvent paymentEvent = PaymentEvent.paymentCompleted(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentMethod()
        );

        kafkaTemplate.send(paymentCompletedTopic, payment.getOrderId().toString(), paymentEvent);
        log.info("Payment completed event sent for order: {}", payment.getOrderId());

        // Update order status via REST call
        updateOrderStatus(payment.getOrderId(), "PAID");

        return modelMapper.map(payment, PaymentDTO.class);
    }

    @Override
    public PaymentDTO getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentException("Payment not found with id: " + id));

        return modelMapper.map(payment, PaymentDTO.class);
    }

    @Override
    public List<PaymentDTO> getPaymentsByOrderId(UUID orderId) {
        List<Payment> payments = paymentRepository.findByOrderId(orderId);

        return payments.stream()
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentDTO processRefund(RefundRequest refundRequest) {
        Payment payment = paymentRepository.findById(refundRequest.getPaymentId())
                .orElseThrow(() -> new PaymentException("Payment not found with id: " + refundRequest.getPaymentId()));

        // Check if payment can be refunded
        if (!"COMPLETED".equals(payment.getStatus())) {
            throw new PaymentException("Only completed payments can be refunded");
        }

        // Check if refund amount is valid
        if (refundRequest.getAmount().compareTo(payment.getAmount()) > 0) {
            throw new PaymentException("Refund amount cannot be greater than payment amount");
        }

        // Process the refund (in a real system, this would integrate with a payment gateway)
        Refund refund = Refund.builder()
                .paymentId(payment.getId())
                .amount(refundRequest.getAmount())
                .reason(refundRequest.getReason())
                .status("COMPLETED")
                .refundDate(LocalDateTime.now())
                .build();

        refundRepository.save(refund);

        // Update payment status
        payment.setStatus("REFUNDED");
        payment = paymentRepository.save(payment);

        // Notify order service about the refund
        PaymentEvent paymentEvent = PaymentEvent.paymentRefunded(
                payment.getId(),
                payment.getOrderId(),
                refundRequest.getAmount()
        );

        kafkaTemplate.send(paymentCompletedTopic, payment.getOrderId().toString(), paymentEvent);

        // Update order status
        updateOrderStatus(payment.getOrderId(), "REFUNDED");

        return modelMapper.map(payment, PaymentDTO.class);
    }

    @Override
    @Transactional
    public PaymentDTO cancelPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException("Payment not found with id: " + paymentId));

        // Check if payment can be cancelled
        if (!"PENDING".equals(payment.getStatus()) && !"COMPLETED".equals(payment.getStatus())) {
            throw new PaymentException("Payment cannot be cancelled in its current state");
        }

        // Cancel the payment
        payment.setStatus("CANCELLED");
        payment = paymentRepository.save(payment);

        return modelMapper.map(payment, PaymentDTO.class);
    }

    /**
     * Process payment with external payment gateway
     * This is a simplified simulation
     */
    private Payment processPaymentWithGateway(PaymentRequest paymentRequest) {
        // In a real implementation, this would call an external payment gateway API
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Simulate some payment gateway logic
        boolean paymentSuccessful = simulatePaymentGateway(paymentRequest);

        if (!paymentSuccessful) {
            Payment failedPayment = Payment.builder()
                    .orderId(paymentRequest.getOrderId())
                    .amount(paymentRequest.getAmount())
                    .paymentMethod(paymentRequest.getPaymentMethod())
                    .status("FAILED")
                    .paymentDate(LocalDateTime.now())
                    .build();

            PaymentEvent paymentEvent = PaymentEvent.paymentFailed(
                    paymentRequest.getOrderId(),
                    paymentRequest.getAmount(),
                    paymentRequest.getPaymentMethod()
            );

            kafkaTemplate.send(paymentCompletedTopic, paymentRequest.getOrderId().toString(), paymentEvent);

            throw new PaymentException("Payment processing failed");
        }

        return Payment.builder()
                .orderId(paymentRequest.getOrderId())
                .amount(paymentRequest.getAmount())
                .paymentMethod(paymentRequest.getPaymentMethod())
                .transactionId(transactionId)
                .status("COMPLETED")
                .paymentDate(LocalDateTime.now())
                .build();
    }

    /**
     * Simulate payment gateway processing
     * In a real implementation, this would be replaced with actual gateway calls
     */
    private boolean simulatePaymentGateway(PaymentRequest paymentRequest) {
        // Simplified payment processing simulation
        // In a real application, this would call an external payment provider API

        // Simulate payment failure for certain scenarios (e.g., card number ending with '0000')
        if (paymentRequest.getCardNumber() != null && paymentRequest.getCardNumber().endsWith("0000")) {
            log.info("Simulating payment failure for test card ending with 0000");
            return false;
        }

        // Simulate successful payment
        log.info("Payment processed successfully through simulated gateway");
        return true;
    }

    /**
     * Get order details from the order service
     */
    private OrderDTO getOrderDetails(UUID orderId) {
        try {
            String url = orderServiceUrl + "/" + orderId;

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity = new HttpEntity<>(headers);

            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    OrderDTO.class
            ).getBody();
        } catch (Exception e) {
            log.error("Error fetching order details: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Update order status in the order service
     */
    private void updateOrderStatus(UUID orderId, String status) {
        try {
            String url = orderServiceUrl + "/" + orderId + "/status";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> statusUpdate = Collections.singletonMap("status", status);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(statusUpdate, headers);

            restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    Void.class
            );

            log.info("Order status updated to {} for order: {}", status, orderId);
        } catch (Exception e) {
            log.error("Error updating order status: {}", e.getMessage());
        }
    }
}