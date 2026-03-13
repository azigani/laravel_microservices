package com.gesco.audit.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEventDto {
    private String userId;
    private String action;
    private String service;
    private String resource;
    private String resourceId;
    private Map<String, Object> before;
    private Map<String, Object> after;
    private Map<String, String> metadata;
}
