package com.bookstore.paymentservice.event;

import com.bookstore.paymentservice.dto.PaymentRequest;
import com.bookstore.paymentservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class OrderEventConsumer {

    private final PaymentService paymentService;

    @Autowired
    public OrderEventConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "${kafka.topics.order-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderCreatedEvent(Map<String, Object> orderEvent) {
        try {
            log.info("Received order created event: {}", orderEvent);

            if (!"ORDER_CREATED".equals(orderEvent.get("eventType"))) {
                log.info("Not an order created event, ignoring: {}", orderEvent.get("eventType"));
                return;
            }

            // Extract data from the event
            Map<String, Object> orderData = (Map<String, Object>) orderEvent.get("order");

            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setOrderId(java.util.UUID.fromString(orderData.get("id").toString()));
            paymentRequest.setAmount(new java.math.BigDecimal(orderData.get("totalAmount").toString()));
            paymentRequest.setPaymentMethod(orderData.get("paymentMethod") != null ?
                    orderData.get("paymentMethod").toString() : "CASH");

            // Process the payment
            paymentService.processPayment(paymentRequest);

        } catch (Exception e) {
            log.error("Error processing order created event", e);
        }
    }
}