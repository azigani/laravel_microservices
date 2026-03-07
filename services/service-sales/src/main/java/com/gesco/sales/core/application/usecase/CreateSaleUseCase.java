package com.gesco.sales.core.application.usecase;

import com.gesco.sales.core.application.dto.SaleRequestDto;
import com.gesco.sales.core.application.dto.SaleResponseDto;
import com.gesco.sales.core.application.mapper.SaleMapper;
import com.gesco.sales.core.domain.model.Sale;
import com.gesco.sales.core.domain.port.SaleEventPublisher;
import com.gesco.sales.core.domain.port.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateSaleUseCase {
    private final SaleRepository saleRepository;
    private final SaleEventPublisher eventPublisher;
    private final SaleMapper saleMapper;

    @Transactional
    public SaleResponseDto execute(SaleRequestDto request) {
        Sale sale = saleMapper.toDomain(request);
        Sale savedSale = saleRepository.save(sale);
        eventPublisher.publishSaleCreated(savedSale);
        return saleMapper.toResponseDto(savedSale);
    }
}
