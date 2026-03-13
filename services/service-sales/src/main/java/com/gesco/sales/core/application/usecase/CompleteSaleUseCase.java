package com.gesco.sales.core.application.usecase;

import com.gesco.sales.core.domain.model.Sale;
import com.gesco.sales.core.domain.port.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompleteSaleUseCase {
    private final SaleRepository saleRepository;

    @Transactional
    public void execute(UUID saleId) {
        log.info("Completing sale with ID: {}", saleId);
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found: " + saleId));
        
        sale.complete();
        saleRepository.save(sale);
        log.info("Sale {} marked as COMPLETED", saleId);
    }
}
