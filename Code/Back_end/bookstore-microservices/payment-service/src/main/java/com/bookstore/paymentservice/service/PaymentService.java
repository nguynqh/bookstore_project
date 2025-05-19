package com.bookstore.paymentservice.service;

import com.bookstore.paymentservice.dto.PaymentDTO;
import com.bookstore.paymentservice.dto.PaymentRequest;
import com.bookstore.paymentservice.dto.RefundRequest;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    /**
     * Process a payment
     *
     * @param paymentRequest the payment request
     * @return the processed payment
     */
    PaymentDTO processPayment(PaymentRequest paymentRequest);

    /**
     * Get a payment by ID
     *
     * @param id the payment ID
     * @return the payment
     */
    PaymentDTO getPaymentById(UUID id);

    /**
     * Get all payments for an order
     *
     * @param orderId the order ID
     * @return list of payments
     */
    List<PaymentDTO> getPaymentsByOrderId(UUID orderId);

    /**
     * Process a refund
     *
     * @param refundRequest the refund request
     * @return the updated payment after refund
     */
    PaymentDTO processRefund(RefundRequest refundRequest);

    /**
     * Cancel a payment
     *
     * @param paymentId the payment ID
     * @return the cancelled payment
     */
    PaymentDTO cancelPayment(UUID paymentId);
}