package com.gesco.sales.infrastructure.adapters.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleCreatedEvent {
    private String orderId; // Matches 'orderId' in Notification service
    private String customerId;
    private List<ItemEvent> items;
    private BigDecimal totalAmount;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemEvent {
        private String productId;
        private int quantity;
    }
}
