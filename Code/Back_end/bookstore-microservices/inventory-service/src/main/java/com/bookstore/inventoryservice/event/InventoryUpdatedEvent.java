package com.bookstore.inventoryservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryUpdatedEvent {
    private UUID bookId;
    private Integer newQuantity;
    private Integer previousQuantity;
    private Boolean lowStock;
    private LocalDateTime updatedAt;
}