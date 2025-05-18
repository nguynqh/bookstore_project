package com.bookstore.inventoryservice.controller;

import com.bookstore.inventoryservice.dto.InventoryDTO;
import com.bookstore.inventoryservice.dto.InventoryTransactionDTO;
import com.bookstore.inventoryservice.dto.InventoryUpdateRequest;
import com.bookstore.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<Page<InventoryDTO>> getAllInventory(Pageable pageable) {
        return ResponseEntity.ok(inventoryService.getAllInventory(pageable));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<InventoryDTO> getInventoryByBookId(@PathVariable UUID bookId) {
        return ResponseEntity.ok(inventoryService.getInventoryByBookId(bookId));
    }

    @PostMapping
    public ResponseEntity<InventoryDTO> initializeInventory(
            @RequestParam UUID bookId,
            @RequestParam Integer initialQuantity,
            @RequestParam(required = false) Integer threshold,
            @RequestParam(required = false) String location) {

        return new ResponseEntity<>(
                inventoryService.initializeInventory(bookId, initialQuantity, threshold, location),
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<InventoryDTO> updateInventory(@Valid @RequestBody InventoryUpdateRequest request) {
        return ResponseEntity.ok(inventoryService.updateInventory(request));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam UUID bookId,
            @RequestParam Integer quantity) {

        boolean isAvailable = inventoryService.checkAvailability(bookId, quantity);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryDTO>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @GetMapping("/{bookId}/transactions")
    public ResponseEntity<Page<InventoryTransactionDTO>> getTransactionHistory(
            @PathVariable UUID bookId,
            Pageable pageable) {

        return ResponseEntity.ok(inventoryService.getTransactionHistory(bookId, pageable));
    }

    @GetMapping("/transactions/reference/{referenceId}")
    public ResponseEntity<List<InventoryTransactionDTO>> getTransactionsByReference(
            @PathVariable UUID referenceId) {

        return ResponseEntity.ok(inventoryService.getTransactionsByReference(referenceId));
    }
}