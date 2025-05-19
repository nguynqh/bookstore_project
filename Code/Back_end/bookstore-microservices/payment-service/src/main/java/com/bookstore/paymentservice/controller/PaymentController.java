package com.bookstore.paymentservice.controller;

import com.bookstore.paymentservice.dto.PaymentDTO;
import com.bookstore.paymentservice.dto.PaymentRequest;
import com.bookstore.paymentservice.dto.RefundRequest;
import com.bookstore.paymentservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        log.info("Received payment request for order: {}", paymentRequest.getOrderId());
        PaymentDTO payment = paymentService.processPayment(paymentRequest);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable UUID id) {
        log.info("Fetching payment details for id: {}", id);
        PaymentDTO payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByOrderId(@PathVariable UUID orderId) {
        log.info("Fetching payments for order: {}", orderId);
        List<PaymentDTO> payments = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentDTO> processRefund(
            @PathVariable UUID id,
            @Valid @RequestBody RefundRequest refundRequest) {
        log.info("Processing refund for payment: {}", id);
        // Ensure the payment ID in path matches the one in request
        refundRequest.setPaymentId(id);
        PaymentDTO payment = paymentService.processRefund(refundRequest);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<PaymentDTO> cancelPayment(@PathVariable UUID id) {
        log.info("Cancelling payment: {}", id);
        PaymentDTO payment = paymentService.cancelPayment(id);
        return ResponseEntity.ok(payment);
    }
}