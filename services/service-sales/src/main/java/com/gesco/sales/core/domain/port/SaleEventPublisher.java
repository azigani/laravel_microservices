package com.gesco.sales.core.domain.port;

import com.gesco.sales.core.domain.model.Sale;

public interface SaleEventPublisher {
    void publishSaleCreated(Sale sale);
}
