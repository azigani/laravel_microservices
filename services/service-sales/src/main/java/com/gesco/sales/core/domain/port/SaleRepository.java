package com.gesco.sales.core.domain.port;

import com.gesco.sales.core.domain.model.Sale;
import java.util.Optional;
import java.util.UUID;

public interface SaleRepository {
    Sale save(Sale sale);

    Optional<Sale> findById(UUID id);
}
