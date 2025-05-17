package com.bookstore.inventoryservice.repository;

import com.bookstore.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    Optional<Inventory> findByBookId(UUID bookId);

    List<Inventory> findByQuantityLessThanEqual(Integer threshold);

    boolean existsByBookId(UUID bookId);
}