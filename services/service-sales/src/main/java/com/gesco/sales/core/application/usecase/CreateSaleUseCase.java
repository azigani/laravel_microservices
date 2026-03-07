package com.gesco.sales.core.application.usecase;

import com.gesco.sales.core.application.dto.SaleRequestDto;
import com.gesco.sales.core.domain.model.Sale;
import com.gesco.sales.core.domain.port.SaleEventPublisher;
import com.gesco.sales.core.domain.port.SaleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CreateSaleUseCase {
        private final SaleRepository saleRepository;
        private final SaleEventPublisher eventPublisher;

        public CreateSaleUseCase(SaleRepository saleRepository, SaleEventPublisher eventPublisher) {
                this.saleRepository = saleRepository;
                this.eventPublisher = eventPublisher;
        }

        public Sale execute(SaleRequestDto request) {
                Sale sale = Sale.builder()
                                .id(UUID.randomUUID())
                                .customerId(request.getCustomerId())
                                .items(request.getItems().stream()
                                                .map(item -> Sale.SaleItem.builder()
                                                                .productId(item.getProductId())
                                                                .quantity(item.getQuantity())
                                                                .price(item.getPrice())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .totalAmount(request.getTotalAmount())
                                .status("COMPLETED")
                                .createdAt(LocalDateTime.now())
                                .build();

                Sale savedSale = saleRepository.save(sale);
                eventPublisher.publishSaleCreated(savedSale);
                return savedSale;
        }
}
