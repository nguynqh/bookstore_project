package com.bookstore.orderservice.config;

import com.bookstore.orderservice.dto.CustomerDTO;
import com.bookstore.orderservice.service.client.CustomerServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Slf4j
public class CustomerServiceClientFallback implements CustomerServiceClient {
    @Override
    public CustomerDTO getCustomerById(@PathVariable UUID id) {
        log.error("Unknown Customer (Service Unavailable)");
        return null;
    }
}
