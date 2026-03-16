package com.gesco.pricing.core.domain.port;

import com.gesco.pricing.core.domain.model.Discount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiscountRepository {
    Discount save(Discount discount);
    Optional<Discount> findById(UUID id);
    Optional<Discount> findByCodeAndActiveTrue(String code);
    List<Discount> findAllActive();
}
