package com.gesco.document.infrastructure.adapters.messaging;

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
public class SaleCreatedEventDto {
    private UUID saleId;
    private String customerId;
    private BigDecimal totalAmount;
    private LocalDateTime timestamp;
}
