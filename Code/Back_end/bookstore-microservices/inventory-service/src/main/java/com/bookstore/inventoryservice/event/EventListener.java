package com.bookstore.inventoryservice.event;

import com.bookstore.inventoryservice.dto.InventoryUpdateRequest;
import com.bookstore.inventoryservice.entity.InventoryTransaction;
import com.bookstore.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventListener {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "${bookstore.kafka.topics.order-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for order ID: {}", event.getOrderId());

        event.getItems().forEach(item -> {
            try {
                InventoryUpdateRequest updateRequest = new InventoryUpdateRequest();
                updateRequest.setBookId(item.getBookId());
                updateRequest.setTransactionType(InventoryTransaction.TransactionType.OUT);
                updateRequest.setQuantity(item.getQuantity());
                updateRequest.setReason("Order: " + event.getOrderId());
                updateRequest.setReferenceId(event.getOrderId());

                inventoryService.updateInventory(updateRequest);
                log.info("Inventory updated for book ID: {}", item.getBookId());
            } catch (Exception e) {
                log.error("Failed to update inventory for book ID: {}", item.getBookId(), e);
                // In a production environment, you might want to implement compensating transactions
                // or notify admin about the inventory update failure
            }
        });
    }
}