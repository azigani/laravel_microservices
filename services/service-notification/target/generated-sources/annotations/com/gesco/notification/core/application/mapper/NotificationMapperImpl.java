package com.gesco.notification.core.application.mapper;

import com.gesco.notification.core.application.dto.OrderPlacedEventDto;
import com.gesco.notification.core.domain.model.Notification;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-07T17:50:22+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public Notification toDomain(OrderPlacedEventDto dto) {
        if ( dto == null ) {
            return null;
        }

        Notification.NotificationBuilder notification = Notification.builder();

        notification.recipient( idToEmail( dto.getCustomerId() ) );

        notification.id( java.util.UUID.randomUUID() );
        notification.subject( "Confirmation de commande GESCO #" + dto.getOrderId() );
        notification.content( String.format("Merci pour votre commande de %.2f €. Elle est en cours de traitement.", dto.getTotalAmount()) );
        notification.createdAt( java.time.LocalDateTime.now() );
        notification.status( Notification.NotificationStatus.PENDING );

        return notification.build();
    }
}
