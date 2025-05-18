package com.bookstore.bookservice.event;

import com.bookstore.bookservice.dto.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEvent {

    private String eventType; // CREATED, UPDATED, DELETED
    private BookDTO book;
    private LocalDateTime timestamp;
}