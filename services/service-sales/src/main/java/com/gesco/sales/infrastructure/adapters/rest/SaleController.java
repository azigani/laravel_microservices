package com.gesco.sales.infrastructure.adapters.rest;

import com.gesco.sales.core.application.dto.SaleRequestDto;
import com.gesco.sales.core.application.dto.SaleResponseDto;
import com.gesco.sales.core.application.mapper.SaleMapper;
import com.gesco.sales.core.application.usecase.CreateSaleUseCase;
import com.gesco.sales.core.domain.port.SaleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {
    private final CreateSaleUseCase createSaleUseCase;
    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;

    @PostMapping
    public ResponseEntity<SaleResponseDto> createSale(@Valid @RequestBody SaleRequestDto request) {
        SaleResponseDto response = createSaleUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDto> getSale(@PathVariable UUID id) {
        return saleRepository.findById(id)
                .map(saleMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

