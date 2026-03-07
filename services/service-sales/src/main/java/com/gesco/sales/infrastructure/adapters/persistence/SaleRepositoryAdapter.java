package com.gesco.sales.infrastructure.adapters.persistence;

import com.gesco.sales.core.domain.model.Sale;
import com.gesco.sales.core.domain.port.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SaleRepositoryAdapter implements SaleRepository {
    private final JpaSaleRepository jpaSaleRepository;

    @Override
    public Sale save(Sale sale) {
        SaleEntity entity = SaleEntity.fromDomain(sale);
        return jpaSaleRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Sale> findById(UUID id) {
        return jpaSaleRepository.findById(id).map(SaleEntity::toDomain);
    }
}
