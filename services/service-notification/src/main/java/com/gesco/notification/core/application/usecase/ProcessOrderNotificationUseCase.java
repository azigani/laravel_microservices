package com.gesco.notification.core.application.usecase;

import com.gesco.notification.core.application.dto.OrderPlacedEventDto;
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

    public void execute(OrderPlacedEventDto event) {
        // Logic: Construct notification domain object
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .recipient(EmailAddress.of("customer@example.com")) // Hardcoded for demo, normally fetched via customerId
                .subject("Confirmation de commande GESCO #" + event.getOrderId())
                .content(String.format("Merci pour votre commande de %.2f €. Elle est en cours de traitement.", event.getTotalAmount()))
                .createdAt(LocalDateTime.now())
                .status(Notification.NotificationStatus.PENDING)
                .build();

        // Send via port
        try {
            emailSender.send(notification);
            notification.markAsSent();
        } catch (Exception e) {
            notification.markAsFailed();
            throw e;
        }
    }
}
