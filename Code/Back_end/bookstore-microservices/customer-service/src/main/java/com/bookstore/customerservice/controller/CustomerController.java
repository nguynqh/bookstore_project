package com.bookstore.customerservice.controller;

import com.bookstore.customerservice.dto.CustomerDTO;
import com.bookstore.customerservice.dto.CustomerRequest;
import com.bookstore.customerservice.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(Pageable pageable) {
        log.info("GET request to fetch all customers");
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable UUID id) {
        log.info("GET request to fetch customer with ID: {}", id);
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CustomerDTO>> searchCustomers(@RequestParam String query, Pageable pageable) {
        log.info("GET request to search customers with query: {}", query);
        return ResponseEntity.ok(customerService.searchCustomers(query, pageable));
    }

    @GetMapping("/type/{customerType}")
    public ResponseEntity<Page<CustomerDTO>> getCustomersByType(
            @PathVariable String customerType,
            Pageable pageable) {
        log.info("GET request to fetch customers by type: {}", customerType);
        return ResponseEntity.ok(customerService.getCustomersByType(customerType, pageable));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        log.info("POST request to create a new customer");
        CustomerDTO createdCustomer = customerService.createCustomer(customerRequest);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody CustomerRequest customerRequest) {
        log.info("PUT request to update customer with ID: {}", id);
        return ResponseEntity.ok(customerService.updateCustomer(id, customerRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        log.info("DELETE request to remove customer with ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/loyalty-points")
    public ResponseEntity<CustomerDTO> updateLoyaltyPoints(
            @PathVariable UUID id,
            @RequestParam Integer points) {
        log.info("PUT request to update loyalty points for customer ID: {}, points: {}", id, points);
        return ResponseEntity.ok(customerService.updateLoyaltyPoints(id, points));
    }
}