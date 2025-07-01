package org.me.practise.personaggregator.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "custom.retry")
@Data
public class RetryProperties {

    private int maxAttempts = 3;
    private int initialInterval = 1000; // in milliseconds
    private double multiplier = 2.0;
    private int maxInterval = 10000; // in milliseconds

    private ServiceUrl personService;
    private ServiceUrl addressService;

    @Data
    public static class ServiceUrl {
        private String url;
    }
}
