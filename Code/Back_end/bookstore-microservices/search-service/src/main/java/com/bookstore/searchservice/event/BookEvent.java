package com.bookstore.searchservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookEvent {

    private String eventType; // CREATED, UPDATED, DELETED

    private BookEventData book;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookEventData {
        private String id;
        private String title;
        private String isbn;
        private String author;
        private String publisher;
        private BigDecimal sellingPrice;
        private LocalDate publicationDate;
        private String description;
        private String imageUrl;
        private Integer pages;
        private Set<CategoryData> categories = new HashSet<>();
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryData {
        private String id;
        private String name;
        private String description;
    }
}