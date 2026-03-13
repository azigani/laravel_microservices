package com.gesco.sales.core.application.usecase;

import com.gesco.sales.core.application.dto.SaleResponseDto;
import com.gesco.sales.core.application.mapper.SaleMapper;
import com.gesco.sales.core.domain.model.Sale;
import com.gesco.sales.core.domain.port.SaleRepository;
import com.gesco.sales.infrastructure.adapters.messaging.RabbitMQSalePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelSaleUseCase {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;
    private final RabbitMQSalePublisher salePublisher;

    @Transactional
    public SaleResponseDto execute(UUID saleId) {
        log.info("Cancelling sale: {}", saleId);

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        if ("CANCELLED".equals(sale.getStatus())) {
            throw new RuntimeException("Sale is already cancelled");
        }

        sale.setStatus("CANCELLED");
        Sale savedSale = saleRepository.save(sale);

        // Publish event for stock rollback
        salePublisher.publishSaleCreatedEvent(saleMapper.toEventDto(savedSale));

        return saleMapper.toResponseDto(savedSale);
    }
}
