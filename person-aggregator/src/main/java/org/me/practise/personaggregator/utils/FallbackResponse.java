package org.me.practise.personaggregator.utils;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FallbackResponse {

    public Map<String, Object> getFallbackResponse(String serviceName, Exception exception) {

        return Map.of (serviceName, Map.of (
                "status", "error",
                "message", "Service is currently unavailable",
                "error", exception.getClass().getSimpleName() + ": " + exception.getMessage()
        ));
    }
}
