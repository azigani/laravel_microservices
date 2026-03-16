package com.gesco.pricing.infrastructure.adapters.persistence;

import com.gesco.pricing.core.domain.model.Discount;
import com.gesco.pricing.core.domain.model.Price;
import com.gesco.pricing.core.domain.port.DiscountRepository;
import com.gesco.pricing.core.domain.port.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PricingPersistenceAdapter implements PriceRepository, DiscountRepository {

    private final JpaPriceRepository priceRepository;
    private final JpaDiscountRepository discountRepository;

    @Override
    public Price save(Price price) {
        return priceRepository.save(price);
    }

    @Override
    public Optional<Price> findById(UUID id) {
        return priceRepository.findById(id);
    }

    @Override
    public Optional<Price> findByProductIdAndActiveTrue(Long productId) {
        return priceRepository.findByProductIdAndActiveTrue(productId);
    }

    @Override
    public Discount save(Discount discount) {
        return discountRepository.save(discount);
    }

    @Override
    public Optional<Discount> findByCodeAndActiveTrue(String code) {
        return discountRepository.findByCodeAndActiveTrue(code);
    }

    @Override
    public List<Discount> findAllActive() {
        return discountRepository.findByActiveTrue();
    }
}
