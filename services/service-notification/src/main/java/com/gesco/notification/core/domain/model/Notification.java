package com.gesco.notification.core.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class Notification {
    private final UUID id;
    private final EmailAddress recipient;
    private final String subject;
    private final String content;
    private final LocalDateTime createdAt;
    private NotificationStatus status;

    public enum NotificationStatus {
        PENDING, SENT, FAILED
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
    }

    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
    }
}
