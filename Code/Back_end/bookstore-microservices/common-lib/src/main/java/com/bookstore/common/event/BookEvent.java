package com.bookstore.common.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEvent {

    public enum EventType {
        CREATED, UPDATED, DELETED
    }

    private UUID bookId;
    private String title;
    private String isbn;
    private String author;
    private String publisher;
    private BigDecimal sellingPrice;
    private BigDecimal costPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    private String description;
    private String imageUrl;
    private Integer pages;
    private Set<UUID> categoryIds;
    private EventType eventType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;
}