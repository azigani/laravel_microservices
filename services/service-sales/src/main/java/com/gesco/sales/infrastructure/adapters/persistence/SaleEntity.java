package com.gesco.sales.infrastructure.adapters.persistence;

import com.gesco.sales.core.domain.model.Sale;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sales")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sale.SaleStatus status;

    public static SaleEntity fromDomain(Sale sale) {
        return SaleEntity.builder()
                .id(sale.getId())
                .customerId(sale.getCustomerId())
                .totalAmount(sale.getTotalAmount())
                .createdAt(sale.getCreatedAt())
                .status(sale.getStatus())
                .build();
    }

    public Sale toDomain() {
        return Sale.builder()
                .id(this.id)
                .customerId(this.customerId)
                .totalAmount(this.totalAmount)
                .createdAt(this.createdAt)
                .status(this.status)
                .build();
    }
}
