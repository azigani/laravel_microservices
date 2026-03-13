package com.gesco.sales.infrastructure.adapters.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Feign client to call service-document via Eureka (no hardcoded URL).
 * Uses Resilience4j Circuit Breaker for fault tolerance.
 *
 * The name "service-document" matches the spring.application.name of the
 * document service.
 * Eureka resolves this name dynamically to the actual IP:port.
 */
@FeignClient(name = "service-document", fallback = DocumentServiceClientFallback.class)
public interface DocumentServiceClient {

    @GetMapping("/api/documents/linked/{saleId}")
    List<Map<String, Object>> getDocumentsBySaleId(@PathVariable("saleId") UUID saleId);
}
