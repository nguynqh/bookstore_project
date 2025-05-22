package com.bookstore.searchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchDTO {

    private String id;
    private String title;
    private String isbn;
    private String author;
    private String publisher;
    private BigDecimal price;
    private LocalDate publicationDate;
    private String description;
    private String imageUrl;
    private Integer pages;
    private Set<CategoryDTO> categories = new HashSet<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDTO {
        private String id;
        private String name;
        private String description;
    }
}