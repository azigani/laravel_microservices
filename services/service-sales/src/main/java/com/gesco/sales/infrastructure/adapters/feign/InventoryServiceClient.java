package com.gesco.sales.infrastructure.adapters.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Feign client to call inventory-service via Eureka.
 * Allows the Sales service to check product availability before creating a
 * sale.
 */
@FeignClient(name = "inventory-service", fallback = InventoryServiceClientFallback.class)
public interface InventoryServiceClient {

    @GetMapping("/api/products/{productId}")
    Map<String, Object> getProductById(@PathVariable("productId") UUID productId);
}
