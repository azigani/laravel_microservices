package com.gesco.pricing.core.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "discounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String code; // e.g., "SUMMER2024"
    
    private BigDecimal percentage; // e.g., 10.00 for 10%
    
    private BigDecimal flatAmount; // fix reduction if percentage is null
    
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    private boolean active;
}
