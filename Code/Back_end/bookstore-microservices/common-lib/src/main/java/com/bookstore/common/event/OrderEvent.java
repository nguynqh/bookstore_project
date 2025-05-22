package com.bookstore.common.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEvent {

    public enum EventType {
        CREATED, UPDATED, CANCELED, COMPLETED
    }

    public enum OrderStatus {
        PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELED, RETURNED
    }

    private UUID orderId;
    private UUID customerId;
    private UUID employeeId;
    private BigDecimal totalAmount;
    private OrderStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime orderDate;

    private String shippingAddress;
    private String paymentMethod;
    private EventType eventType;
    private List<OrderItemEvent> items;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemEvent {
        private UUID bookId;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal discount;
    }
}