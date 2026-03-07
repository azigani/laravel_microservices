package com.gesco.sales.infrastructure.adapters.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    private String customerId;

    @ElementCollection
    @CollectionTable(name = "sale_items", joinColumns = @JoinColumn(name = "sale_id"))
    private List<SaleItemEmbeddable> items;

    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaleItemEmbeddable {
        private String productId;
        private int quantity;
        private BigDecimal price;
    }
}
