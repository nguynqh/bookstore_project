package com.bookstore.orderservice.config;

import com.bookstore.orderservice.dto.InventoryDTO;
import com.bookstore.orderservice.service.client.InventoryServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Slf4j
public class InventoryServiceClientFallback implements InventoryServiceClient {
    @Override
    public boolean checkStock(@RequestParam UUID bookId, @RequestParam int quantity) {
        log.info("Check stock for bookId: {}, quantity: {}", bookId, quantity);
        return false;
    }
}
