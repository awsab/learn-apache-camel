package org.me.practise.personaggregator.utils;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceResolver {

    public String resolveServiceUrl(List<String> serviceNames) {
        /*return serviceNames.stream()
                .map(serviceName -> "direct:" + serviceName)
                .peek ( serviceName -> System.out.println("Resolved service URL: " + serviceName))
                .collect( Collectors.joining(","));*/
        return String.join(",", serviceNames);

    }
}
