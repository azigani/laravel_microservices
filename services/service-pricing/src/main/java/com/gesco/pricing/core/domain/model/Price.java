package com.gesco.pricing.core.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "prices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long productId;
    
    @Column(nullable = false)
    private BigDecimal basePrice;
    
    @Column(length = 3)
    private String currency; // XOF, EUR, USD
    
    @Column(nullable = false)
    private boolean active;
}
