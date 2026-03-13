package com.gesco.payment.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleCreatedDto {
    private Long id;
    private String customerId;
    private BigDecimal totalAmount;
    private String status;
}
