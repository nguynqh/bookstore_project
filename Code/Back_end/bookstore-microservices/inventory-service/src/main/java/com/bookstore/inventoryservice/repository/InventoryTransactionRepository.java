package com.bookstore.inventoryservice.repository;

import com.bookstore.inventoryservice.entity.InventoryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, UUID> {

    Page<InventoryTransaction> findByBookId(UUID bookId, Pageable pageable);

    List<InventoryTransaction> findByReferenceId(UUID referenceId);

    List<InventoryTransaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
}