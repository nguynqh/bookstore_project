package com.bookstore.orderservice.service.client;

import com.bookstore.orderservice.config.InventoryServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "inventory-service",
             url = "${inventory-service.url:http://localhost:8084}",
             fallback = InventoryServiceClientFallback.class)
public interface InventoryServiceClient {

    @GetMapping("/api/inventory/check")
    boolean checkStock(@RequestParam UUID bookId, @RequestParam int quantity);
}