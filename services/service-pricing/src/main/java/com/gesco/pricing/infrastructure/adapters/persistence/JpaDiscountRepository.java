package com.gesco.pricing.infrastructure.adapters.persistence;

import com.gesco.pricing.core.domain.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaDiscountRepository extends JpaRepository<Discount, UUID> {
    Optional<Discount> findByCodeAndActiveTrue(String code);
    List<Discount> findByActiveTrue();
}
