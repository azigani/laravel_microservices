package com.gesco.sales.core.application.mapper;

import com.gesco.sales.core.application.dto.SaleRequestDto;
import com.gesco.sales.core.application.dto.SaleResponseDto;
import com.gesco.sales.core.domain.model.Sale;
import com.gesco.sales.infrastructure.adapters.messaging.SaleCreatedEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", constant = "PENDING")
    Sale toDomain(SaleRequestDto dto);

    SaleResponseDto toResponseDto(Sale sale);

    @Mapping(target = "saleId", source = "id")
    @Mapping(target = "timestamp", source = "createdAt")
    SaleCreatedEventDto toEventDto(Sale sale);
}
