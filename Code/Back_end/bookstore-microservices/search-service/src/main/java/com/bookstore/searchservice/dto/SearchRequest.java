package com.bookstore.searchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    @NotBlank(message = "Search query is required")
    private String query;

    private Set<String> categories;
    private String author;
    private String publisher;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private LocalDate fromDate;
    private LocalDate toDate;

    @PositiveOrZero(message = "Page index must be positive or zero")
    private int page = 0;

    @Positive(message = "Page size must be positive")
    private int size = 10;

    private String sortBy = "relevance";
    private boolean ascending = false;
}