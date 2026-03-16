package com.gesco.pricing.core.application.usecase;

import com.gesco.pricing.core.domain.model.Discount;
import com.gesco.pricing.core.domain.port.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateDiscountUseCase {
    private final DiscountRepository discountRepository;

    @Transactional
    public Discount execute(String code, BigDecimal percentage, BigDecimal flatAmount, LocalDateTime startDate, LocalDateTime endDate) {
        
        if (percentage == null && flatAmount == null) {
            throw new IllegalArgumentException("Either percentage or flatAmount must be provided");
        }
        
        // Deactivate existing discount with the same code if active
        discountRepository.findByCodeAndActiveTrue(code).ifPresent(existingDiscount -> {
            existingDiscount.setActive(false);
            discountRepository.save(existingDiscount);
        });

        Discount newDiscount = Discount.builder()
                .code(code)
                .percentage(percentage)
                .flatAmount(flatAmount)
                .startDate(startDate)
                .endDate(endDate)
                .active(true)
                .build();

        return discountRepository.save(newDiscount);
    }
}
