package com.gesco.pricing.core.application.usecase;

import com.gesco.pricing.core.domain.model.Discount;
import com.gesco.pricing.core.domain.model.Price;
import com.gesco.pricing.core.domain.port.DiscountRepository;
import com.gesco.pricing.core.domain.port.PriceRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalculatePriceUseCase {
    private final PriceRepository priceRepository;
    private final DiscountRepository discountRepository;

    public PriceResult execute(Long productId, String discountCode) {
        Price activePrice = priceRepository.findByProductIdAndActiveTrue(productId)
                .orElseThrow(() -> new RuntimeException("No active price found for product " + productId));

        BigDecimal finalPrice = activePrice.getBasePrice();
        String appliedDiscount = null;

        // Apply specific discount code if provided
        if (discountCode != null && !discountCode.isEmpty()) {
            Optional<Discount> discountOpt = discountRepository.findByCodeAndActiveTrue(discountCode);
            if (discountOpt.isPresent()) {
                Discount discount = discountOpt.get();
                if (isValidNow(discount)) {
                    finalPrice = applyDiscount(finalPrice, discount);
                    appliedDiscount = discountCode;
                }
            }
        } else {
            // Apply the best active global discount if no specific code is provided
            List<Discount> activeDiscounts = discountRepository.findAllActive();
            BigDecimal bestDiscountPrice = finalPrice;
            Discount bestDiscount = null;

            for (Discount discount : activeDiscounts) {
                if (isValidNow(discount)) {
                    BigDecimal discountedPrice = applyDiscount(finalPrice, discount);
                    if (discountedPrice.compareTo(bestDiscountPrice) < 0) {
                        bestDiscountPrice = discountedPrice;
                        bestDiscount = discount;
                    }
                }
            }

            finalPrice = bestDiscountPrice;
            if (bestDiscount != null) {
                appliedDiscount = bestDiscount.getCode();
            }
        }

        return PriceResult.builder()
                .productId(productId)
                .originalPrice(activePrice.getBasePrice())
                .finalPrice(finalPrice)
                .currency(activePrice.getCurrency())
                .appliedDiscountCode(appliedDiscount)
                .build();
    }

    private boolean isValidNow(Discount discount) {
        LocalDateTime now = LocalDateTime.now();
        if (discount.getStartDate() != null && now.isBefore(discount.getStartDate())) {
            return false;
        }
        if (discount.getEndDate() != null && now.isAfter(discount.getEndDate())) {
            return false;
        }
        return true;
    }

    private BigDecimal applyDiscount(BigDecimal basePrice, Discount discount) {
        if (discount.getPercentage() != null) {
            BigDecimal reduction = basePrice.multiply(discount.getPercentage()).divide(new BigDecimal("100"));
            return basePrice.subtract(reduction);
        } else if (discount.getFlatAmount() != null) {
             BigDecimal result = basePrice.subtract(discount.getFlatAmount());
             return result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result;
        }
        return basePrice;
    }

    @Data
    @Builder
    public static class PriceResult {
        private Long productId;
        private BigDecimal originalPrice;
        private BigDecimal finalPrice;
        private String currency;
        private String appliedDiscountCode;
    }
}
