package com.bookstore.orderservice.config;

import com.bookstore.orderservice.dto.BookDTO;
import com.bookstore.orderservice.service.client.BookServiceClient;
import lombok.extern.slf4j.Slf4j;


import java.util.UUID;

@Slf4j
public class BookServiceClientFallback implements BookServiceClient {
    @Override
    public BookDTO getBookById(UUID id) {
//        return new BookDTO(id, "Unknown Book (Service Unavailable)", "", "", BigDecimal.ZERO, "");
        log.error("Unknown Book (Service Unavailable)");
        return null;
    }

}
