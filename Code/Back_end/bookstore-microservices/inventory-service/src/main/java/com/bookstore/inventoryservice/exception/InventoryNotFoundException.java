package com.bookstore.inventoryservice.exception;

import java.util.UUID;

public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(String message) {
        super(message);
    }

    public InventoryNotFoundException(UUID bookId) {
        super("Inventory not found for book ID: " + bookId);
    }
}