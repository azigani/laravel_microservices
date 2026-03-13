package com.gesco.pricing.infrastructure.adapters.rest;

import com.gesco.pricing.core.application.usecase.PricingService;
import com.gesco.pricing.core.domain.model.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    @GetMapping("/calculate")
    public ResponseEntity<BigDecimal> calculate(@RequestParam Long productId, 
                                               @RequestParam BigDecimal basePrice,
                                               @RequestParam(required = false) BigDecimal discount) {
        
        Price price = Price.builder()
                .productId(productId)
                .basePrice(basePrice)
                .build();
        
        BigDecimal finalPrice = pricingService.calculateProductPrice(price, Optional.ofNullable(discount));
        
        return ResponseEntity.ok(finalPrice);
    }
}
