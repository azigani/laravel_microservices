package com.gesco.notification.core.application.usecase;

import com.gesco.notification.core.application.dto.SaleCreatedEventDto;
import com.gesco.notification.core.application.mapper.NotificationMapper;
import com.gesco.notification.core.domain.model.EmailAddress;
import com.gesco.notification.core.domain.model.Notification;
import com.gesco.notification.core.domain.port.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessOrderNotificationUseCase {
    private final EmailSender emailSender;
    private final NotificationMapper notificationMapper;

    public void execute(SaleCreatedEventDto event) {
        // Utilisation du Mapper pour transformer le DTO en Entité Domaine (DDD)
        Notification notification = notificationMapper.toDomain(event);

        // Envoi via le port
        try {
            emailSender.send(notification);
            notification.markAsSent();
        } catch (Exception e) {
            notification.markAsFailed();
            throw e;
        }
    }
}
