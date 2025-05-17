package com.bookstore.inventoryservice.dto;

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
public class InventoryDTO {
    private UUID id;
    private UUID bookId;
    private String bookTitle;  // Optional - can be fetched from Book Service
    private Integer quantity;
    private Integer threshold;
    private String location;
    private LocalDateTime lastRestock;
    private Boolean lowStock;
}