package com.gesco.hub.infrastructure.adapters.rest;

import com.gesco.hub.core.application.usecase.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/hub")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverview() {
        return ResponseEntity.ok(hubService.getSystemOverview());
    }
}
