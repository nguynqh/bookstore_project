package com.bookstore.inventoryservice.exception;

import java.util.UUID;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(UUID bookId, Integer requested, Integer available) {
        super(String.format("Insufficient stock for book ID %s. Requested: %d, Available: %d",
                bookId, requested, available));
    }
}