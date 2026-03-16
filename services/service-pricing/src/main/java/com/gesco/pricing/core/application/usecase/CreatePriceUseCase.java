package com.gesco.pricing.core.application.usecase;

import com.gesco.pricing.core.domain.model.Price;
import com.gesco.pricing.core.domain.port.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreatePriceUseCase {

    private final PriceRepository priceRepository;

    @Transactional
    public Price execute(Long productId, BigDecimal basePrice, String currency) {
        // Deactivate existing active prices for this product
        priceRepository.findByProductIdAndActiveTrue(productId).ifPresent(existingPrice -> {
            existingPrice.setActive(false);
            priceRepository.save(existingPrice);
        });

        Price newPrice = Price.builder()
                .productId(productId)
                .basePrice(basePrice)
                .currency(currency)
                .active(true)
                .build();

        return priceRepository.save(newPrice);
    }
}
