package com.bookstore.bookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private Set<CategoryDTO> categories = new HashSet<>();
}