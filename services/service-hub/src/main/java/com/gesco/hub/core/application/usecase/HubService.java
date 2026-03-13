package com.gesco.hub.core.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HubService {

    private final DiscoveryClient discoveryClient;

    /**
     * Récupère l'état de santé résumé de tous les microservices enregistrés.
     */
    public Map<String, Object> getSystemOverview() {
        Map<String, Object> overview = new HashMap<>();
        List<String> services = discoveryClient.getServices();
        
        overview.put("total_services", services.size());
        overview.put("registered_services", services);
        
        Map<String, String> healthMap = new HashMap<>();
        for (String serviceName : services) {
            int instances = discoveryClient.getInstances(serviceName).size();
            healthMap.put(serviceName, instances > 0 ? "UP (" + instances + " instances)" : "DOWN");
        }
        overview.put("health_status", healthMap);
        
        return overview;
    }
}
