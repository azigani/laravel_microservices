package com.gesco.audit.core.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@Document(collection = "audit_logs")
public class AuditLog {
    @Id
    private String id;
    private LocalDateTime timestamp;
    private String userId;
    private String action;
    private String service;
    private String resource;
    private String resourceId;
    private Map<String, Object> before;
    private Map<String, Object> after;
    private Map<String, String> metadata;
}
