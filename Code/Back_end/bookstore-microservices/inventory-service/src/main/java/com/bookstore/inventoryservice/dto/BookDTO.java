package com.bookstore.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private UUID id;
    private String title;
    private String isbn;
    private String author;
    private String publisher;
    private BigDecimal sellingPrice;
    private BigDecimal costPrice;
    private LocalDate publicationDate;
    private String description;
    private String imageUrl;
    private Integer pages;
}