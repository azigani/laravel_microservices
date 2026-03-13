package com.gesco.audit.core.application.usecase;

import com.gesco.audit.core.domain.model.AuditLog;
import com.gesco.audit.infrastructure.adapters.persistence.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordAuditUseCase {
    private final AuditLogRepository repository;

    public void execute(AuditLog auditLog) {
        if (auditLog.getTimestamp() == null) {
            auditLog.setTimestamp(LocalDateTime.now());
        }
        repository.save(auditLog);
        log.info("Audit log recorded: {} on {}/{}", auditLog.getAction(), auditLog.getService(),
                auditLog.getResource());
    }
}
