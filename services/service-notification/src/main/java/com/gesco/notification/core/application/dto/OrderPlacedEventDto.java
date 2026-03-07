package com.gesco.notification.core.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEventDto {
    private String orderId;
    private String customerId;
    private List<OrderItemDto> items;
    private double totalAmount;

    @Data
    public static class OrderItemDto {
        private String productId;
        private int quantity;
    }
}
