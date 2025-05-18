package com.bookstore.orderservice.service.client;

import com.bookstore.orderservice.config.CustomerServiceClientFallback;
import com.bookstore.orderservice.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "customer-service",
             url = "${customer-service.url:http://localhost:8083}",
             fallback = CustomerServiceClientFallback.class)
public interface CustomerServiceClient {

    @GetMapping("/api/customers/{id}")
    CustomerDTO getCustomerById(@PathVariable UUID id);
}