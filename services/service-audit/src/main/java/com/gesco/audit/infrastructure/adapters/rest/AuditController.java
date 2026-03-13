package com.gesco.audit.infrastructure.adapters.rest;

import com.gesco.audit.core.application.usecase.RecordAuditUseCase;
import com.gesco.audit.core.domain.model.AuditLog;
import com.gesco.audit.core.domain.model.AuditEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {
    private final RecordAuditUseCase recordAuditUseCase;

    @PostMapping
    public void recordAudit(@RequestBody AuditEventDto eventDto) {
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
    }
}
