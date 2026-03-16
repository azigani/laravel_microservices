package com.gesco.pricing.core.domain.port;

import com.gesco.pricing.core.domain.model.Price;

import java.util.Optional;
import java.util.UUID;

public interface PriceRepository {
    Price save(Price price);
    Optional<Price> findById(UUID id);
    Optional<Price> findByProductIdAndActiveTrue(Long productId);
}
