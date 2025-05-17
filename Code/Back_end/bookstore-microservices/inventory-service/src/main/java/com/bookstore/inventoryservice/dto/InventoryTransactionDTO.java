package com.bookstore.inventoryservice.dto;

import com.bookstore.inventoryservice.entity.InventoryTransaction.TransactionType;
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
public class InventoryTransactionDTO {
    private UUID id;
    private UUID bookId;
    private String bookTitle;  // Optional - can be populated from book service
    private TransactionType transactionType;
    private Integer quantity;
    private String reason;
    private UUID referenceId;
    private LocalDateTime transactionDate;
}