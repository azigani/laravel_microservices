package com.gesco.notification.infrastructure.adapters.mail;

import com.gesco.notification.core.domain.model.Notification;
import com.gesco.notification.core.domain.port.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JavaMailSenderAdapter implements EmailSender {
    private final JavaMailSender mailSender;

    @Override
    public void send(Notification notification) {
        log.info("[Mail] Sending email to: {}", notification.getRecipient().getValue());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getRecipient().getValue());
        message.setSubject(notification.getSubject());
        message.setText(notification.getContent());
        
        // Log skip because SMTP might not be configured in docker easily without MailHog/Mailtrap
        log.info("[Mail Simulator] Notification Sent: Subject='{}'", notification.getSubject());
        // mailSender.send(message); // Uncomment when SMTP is ready
    }
}
