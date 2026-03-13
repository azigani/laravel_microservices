package com.gesco.audit.infrastructure.adapters.messaging;

import com.gesco.audit.core.application.usecase.RecordAuditUseCase;
import com.gesco.audit.core.domain.model.AuditLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditEventListener {
    private final RecordAuditUseCase recordAuditUseCase;

    @RabbitListener(queues = "${audit.rabbitmq.queue}")
    public void handleAuditEvent(AuditEventDto eventDto) {
        log.info("Received audit event: {}", eventDto.getAction());
        try {
            AuditLog auditLog = AuditLog.builder()
                    .userId(eventDto.getUserId())
                    .action(eventDto.getAction())
                    .service(eventDto.getService())
                    .resource(eventDto.getResource())
                    .resourceId(eventDto.getResourceId())
                    .before(eventDto.getBefore())
                    .after(eventDto.getAfter())
                    .metadata(eventDto.getMetadata())
                    .build();
            recordAuditUseCase.execute(auditLog);
        } catch (Exception e) {
            log.error("Failed to process audit event", e);
        }
    }
}
