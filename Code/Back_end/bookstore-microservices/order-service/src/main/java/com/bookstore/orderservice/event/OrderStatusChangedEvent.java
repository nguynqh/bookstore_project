package com.bookstore.orderservice.event;

import com.bookstore.orderservice.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusChangedEvent {
    private UUID orderId;
    private UUID customerId;
    private OrderStatus oldStatus;
    private OrderStatus newStatus;
    private LocalDateTime timestamp;
}