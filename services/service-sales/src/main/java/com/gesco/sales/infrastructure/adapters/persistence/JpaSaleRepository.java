package com.gesco.sales.infrastructure.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaSaleRepository extends JpaRepository<SaleEntity, UUID> {
}
