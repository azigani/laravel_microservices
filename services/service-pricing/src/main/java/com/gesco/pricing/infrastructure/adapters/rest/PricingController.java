package com.gesco.pricing.infrastructure.adapters.rest;

import com.gesco.pricing.core.application.usecase.CalculatePriceUseCase;
import com.gesco.pricing.core.application.usecase.CreateDiscountUseCase;
import com.gesco.pricing.core.application.usecase.CreatePriceUseCase;
import com.gesco.pricing.core.domain.model.Discount;
import com.gesco.pricing.core.domain.model.Price;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final CreatePriceUseCase createPriceUseCase;
    private final CreateDiscountUseCase createDiscountUseCase;
    private final CalculatePriceUseCase calculatePriceUseCase;

    @PostMapping("/prices")
    public ResponseEntity<Price> createPrice(@RequestBody CreatePriceRequest request) {
        Price price = createPriceUseCase.execute(request.getProductId(), request.getBasePrice(), request.getCurrency());
        return ResponseEntity.ok(price);
    }

    @PostMapping("/discounts")
    public ResponseEntity<Discount> createDiscount(@RequestBody CreateDiscountRequest request) {
        Discount discount = createDiscountUseCase.execute(
                request.getCode(),
                request.getPercentage(),
                request.getFlatAmount(),
                request.getStartDate(),
                request.getEndDate()
        );
        return ResponseEntity.ok(discount);
    }

    @GetMapping("/calculate")
    public ResponseEntity<CalculatePriceUseCase.PriceResult> calculate(@RequestParam Long productId,
                                                                       @RequestParam(required = false) String discountCode) {
        CalculatePriceUseCase.PriceResult result = calculatePriceUseCase.execute(productId, discountCode);
        return ResponseEntity.ok(result);
    }

    @Data
    public static class CreatePriceRequest {
        private Long productId;
        private BigDecimal basePrice;
        private String currency;
    }

    @Data
    public static class CreateDiscountRequest {
        private String code;
        private BigDecimal percentage;
        private BigDecimal flatAmount;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }
}
