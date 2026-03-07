package com.gesco.notification.core.application.mapper;

import com.gesco.notification.core.application.dto.OrderPlacedEventDto;
import com.gesco.notification.core.domain.model.EmailAddress;
import com.gesco.notification.core.domain.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "recipient", source = "customerId", qualifiedByName = "idToEmail")
    @Mapping(target = "subject", expression = "java(\"Confirmation de commande GESCO #\" + dto.getOrderId())")
    @Mapping(target = "content", expression = "java(String.format(\"Merci pour votre commande de %.2f €. Elle est en cours de traitement.\", dto.getTotalAmount()))")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", constant = "PENDING")
    Notification toDomain(OrderPlacedEventDto dto);

    @Named("idToEmail")
    default EmailAddress idToEmail(String customerId) {
        // Simulation: normalement on appellerait un service client
        return EmailAddress.of(customerId + "@example.com");
    }
}
