package com.gesco.sales.infrastructure.adapters.rest;

import com.gesco.sales.core.application.dto.SaleRequestDto;
import com.gesco.sales.core.application.usecase.CreateSaleUseCase;
import com.gesco.sales.core.domain.model.Sale;
import com.gesco.sales.core.domain.port.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final CreateSaleUseCase createSaleUseCase;
    private final SaleRepository saleRepository;

    public SaleController(CreateSaleUseCase createSaleUseCase, SaleRepository saleRepository) {
        this.createSaleUseCase = createSaleUseCase;
        this.saleRepository = saleRepository;
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody SaleRequestDto request) {
        return ResponseEntity.ok(createSaleUseCase.execute(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSale(@PathVariable UUID id) {
        return saleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
