package com.bookstore.orderservice.controller;

import com.bookstore.orderservice.dto.OrderDTO;
import com.bookstore.orderservice.dto.OrderRequest;
import com.bookstore.orderservice.dto.OrderStatusRequest;
import com.bookstore.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('EMPLOYEE')")
    public ResponseEntity<Page<OrderDTO>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('EMPLOYEE') or @orderAuthorizationService.canAccessOrder(#id)")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.createOrder(orderRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('EMPLOYEE')")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderStatusRequest statusRequest) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, statusRequest.getStatus()));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('EMPLOYEE') or @customerAuthorizationService.canAccessCustomer(#customerId)")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(@PathVariable UUID customerId) {
        return ResponseEntity.ok(orderService.findByCustomerId(customerId));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('EMPLOYEE') or @orderAuthorizationService.canAccessOrder(#id)")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
}