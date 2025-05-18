package com.bookstore.orderservice.dto;

import com.bookstore.orderservice.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequest {
    @NotNull(message = "Status is required")
    private OrderStatus status;
}