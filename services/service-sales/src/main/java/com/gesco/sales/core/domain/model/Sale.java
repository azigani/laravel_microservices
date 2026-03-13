package com.gesco.sales.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    private UUID id;
    private String customerId;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private SaleStatus status;

    public void complete() {
        this.status = SaleStatus.COMPLETED;
    }

    public enum SaleStatus {
        PENDING, COMPLETED, CANCELLED
    }
}
