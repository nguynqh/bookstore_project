package com.bookstore.orderservice.event;

import com.bookstore.orderservice.entity.OrderStatus;
import com.bookstore.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventListener {

    private final OrderService orderService;

    @KafkaListener(topics = "${spring.kafka.topic.payment-completed}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
        log.info("Payment completed for order: {}", event.getOrderId());
        orderService.updateOrderStatus(event.getOrderId(), OrderStatus.CONFIRMED);
    }
}