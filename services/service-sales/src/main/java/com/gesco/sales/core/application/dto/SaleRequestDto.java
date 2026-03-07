package com.gesco.sales.core.application.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleRequestDto {
    private String customerId;
    private List<ItemRequestDto> items;
    private BigDecimal totalAmount;

    @Data
    public static class ItemRequestDto {
        private String productId;
        private int quantity;
        private BigDecimal price;
    }
}
