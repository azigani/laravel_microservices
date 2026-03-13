package com.gesco.sales.infrastructure.adapters.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Fallback implementation for DocumentServiceClient.
 * Called when service-document is unreachable (circuit breaker open).
 */
@Slf4j
@Component
public class DocumentServiceClientFallback implements DocumentServiceClient {

    @Override
    public List<Map<String, Object>> getDocumentsBySaleId(UUID saleId) {
        log.warn("Circuit breaker: service-document is unavailable. Returning empty documents for sale: {}", saleId);
        return Collections.emptyList();
    }
}
