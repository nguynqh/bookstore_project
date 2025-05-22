package com.bookstore.common.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryEvent {

    public enum EventType {
        STOCK_ADDED, STOCK_REMOVED, LOW_STOCK, OUT_OF_STOCK
    }

    private UUID bookId;
    private Integer quantity;
    private Integer currentStock;
    private Integer threshold;
    private String reason;
    private UUID referenceId; // Order ID or Purchase Order ID
    private EventType eventType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;
}