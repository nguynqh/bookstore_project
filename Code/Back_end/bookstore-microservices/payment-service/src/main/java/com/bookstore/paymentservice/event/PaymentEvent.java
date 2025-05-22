package com.bookstore.paymentservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEvent {

    private UUID paymentId;
    private UUID orderId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime paymentDate;
    private String eventType;  // PAYMENT_CREATED, PAYMENT_COMPLETED, PAYMENT_FAILED, PAYMENT_REFUNDED

    public static PaymentEvent paymentCompleted(UUID paymentId, UUID orderId, BigDecimal amount, String paymentMethod) {
        return PaymentEvent.builder()
                .paymentId(paymentId)
                .orderId(orderId)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .status("COMPLETED")
                .paymentDate(LocalDateTime.now())
                .eventType("PAYMENT_COMPLETED")
                .build();
    }

    public static PaymentEvent paymentFailed(UUID orderId, BigDecimal amount, String paymentMethod) {
        return PaymentEvent.builder()
                .orderId(orderId)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .status("FAILED")
                .paymentDate(LocalDateTime.now())
                .eventType("PAYMENT_FAILED")
                .build();
    }

    public static PaymentEvent paymentRefunded(UUID paymentId, UUID orderId, BigDecimal amount) {
        return PaymentEvent.builder()
                .paymentId(paymentId)
                .orderId(orderId)
                .amount(amount)
                .status("REFUNDED")
                .paymentDate(LocalDateTime.now())
                .eventType("PAYMENT_REFUNDED")
                .build();
    }
}