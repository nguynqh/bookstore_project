package com.bookstore.orderservice.service.client;

import com.bookstore.orderservice.config.BookServiceClientFallback;
import com.bookstore.orderservice.dto.BookDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "book-service",
            url = "${book-service.url:http://localhost:8081}",
            fallback = BookServiceClientFallback.class)
public interface BookServiceClient {

    @GetMapping("/api/books/{id}")
    BookDTO getBookById(@PathVariable UUID id);
}