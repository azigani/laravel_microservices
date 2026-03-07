package com.gesco.sales.infrastructure.adapters.persistence;

import com.gesco.sales.core.domain.model.Sale;
import com.gesco.sales.core.domain.port.SaleRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SaleRepositoryAdapter implements SaleRepository {
        private final JpaSaleRepository jpaSaleRepository;

        public SaleRepositoryAdapter(JpaSaleRepository jpaSaleRepository) {
                this.jpaSaleRepository = jpaSaleRepository;
        }

        @Override
        public Sale save(Sale sale) {
                SaleEntity entity = SaleEntity.builder()
                                .id(sale.getId())
                                .customerId(sale.getCustomerId())
                                .items(sale.getItems().stream()
                                                .map(item -> SaleEntity.SaleItemEmbeddable.builder()
                                                                .productId(item.getProductId())
                                                                .quantity(item.getQuantity())
                                                                .price(item.getPrice())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .totalAmount(sale.getTotalAmount())
                                .status(sale.getStatus())
                                .createdAt(sale.getCreatedAt())
                                .build();

                SaleEntity savedEntity = jpaSaleRepository.save(entity);
                return mapToDomain(savedEntity);
        }

        @Override
        public Optional<Sale> findById(UUID id) {
                return jpaSaleRepository.findById(id).map(this::mapToDomain);
        }

        private Sale mapToDomain(SaleEntity entity) {
                return Sale.builder()
                                .id(entity.getId())
                                .customerId(entity.getCustomerId())
                                .items(entity.getItems().stream()
                                                .map(item -> Sale.SaleItem.builder()
                                                                .productId(item.getProductId())
                                                                .quantity(item.getQuantity())
                                                                .price(item.getPrice())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .totalAmount(entity.getTotalAmount())
                                .status(entity.getStatus())
                                .createdAt(entity.getCreatedAt())
                                .build();
        }
}
