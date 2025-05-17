package com.bookstore.inventoryservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "book_id", nullable = false, unique = true)
//    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID bookId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "threshold", nullable = false)
    private Integer threshold;

    @Column(name = "location")
    private String location;

    @Column(name = "last_restock")
    private LocalDateTime lastRestock;
}