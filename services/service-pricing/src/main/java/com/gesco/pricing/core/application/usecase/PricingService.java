package com.gesco.pricing.core.application.usecase;

import com.gesco.pricing.core.domain.model.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PricingService {

    /**
     * Calcule le prix final d'un produit en appliquant les remises si nécessaire.
     * Pour ce projet "wahou", on pourrait imaginer des stratégies complexes.
     */
    public BigDecimal calculateProductPrice(Price price, Optional<BigDecimal> discountPercentage) {
        BigDecimal finalPrice = price.getBasePrice();
        
        if (discountPercentage.isPresent()) {
            BigDecimal reduction = finalPrice.multiply(discountPercentage.get()).divide(new BigDecimal("100"));
            finalPrice = finalPrice.subtract(reduction);
        }
        
        return finalPrice;
    }
}
