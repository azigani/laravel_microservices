package com.gesco.notification.core.domain.port;

import com.gesco.notification.core.domain.model.Notification;

public interface EmailSender {
    void send(Notification notification);
}
