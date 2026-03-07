package com.gesco.sales.core.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponseDto {
    private UUID id;
    private String customerId;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private String status;
}
