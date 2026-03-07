package com.gesco.sales.infrastructure.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JpaSaleRepository extends JpaRepository<SaleEntity, UUID> {
}
