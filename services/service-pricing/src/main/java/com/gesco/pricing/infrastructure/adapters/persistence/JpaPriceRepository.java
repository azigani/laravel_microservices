package com.gesco.pricing.infrastructure.adapters.persistence;

import com.gesco.pricing.core.domain.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPriceRepository extends JpaRepository<Price, UUID> {
    Optional<Price> findByProductIdAndActiveTrue(Long productId);
}
