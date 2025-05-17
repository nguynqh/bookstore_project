package com.bookstore.inventoryservice.service;

import com.bookstore.inventoryservice.dto.InventoryDTO;
import com.bookstore.inventoryservice.dto.InventoryTransactionDTO;
import com.bookstore.inventoryservice.dto.InventoryUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface InventoryService {

    /**
     * Get all inventory items with pagination
     */
    Page<InventoryDTO> getAllInventory(Pageable pageable);

    /**
     * Get inventory by book ID
     */
    InventoryDTO getInventoryByBookId(UUID bookId);

    /**
     * Check if book has sufficient stock
     */
    boolean checkAvailability(UUID bookId, Integer quantity);

    /**
     * Update inventory (add or remove stock)
     */
    InventoryDTO updateInventory(InventoryUpdateRequest request);

    /**
     * Initialize inventory for new book
     */
    InventoryDTO initializeInventory(UUID bookId, Integer initialQuantity, Integer threshold, String location);

    /**
     * Get inventory items with stock below threshold
     */
    List<InventoryDTO> getLowStockItems();

    /**
     * Get transaction history for a book
     */
    Page<InventoryTransactionDTO> getTransactionHistory(UUID bookId, Pageable pageable);

    /**
     * Get transactions for a specific reference (e.g., order ID)
     */
    List<InventoryTransactionDTO> getTransactionsByReference(UUID referenceId);
}