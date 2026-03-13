package com.gesco.sales.infrastructure.adapters.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class InventoryServiceClientFallback implements InventoryServiceClient {

    @Override
    public Map<String, Object> getProductById(UUID productId) {
        log.warn("Circuit breaker: inventory-service is unavailable. Product check skipped for: {}", productId);
        return Collections.emptyMap();
    }
}
