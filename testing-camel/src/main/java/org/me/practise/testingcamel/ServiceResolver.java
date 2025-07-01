package org.me.practise.testingcamel;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceResolver {
    public String resolveServiceUrl(List<String> services) {
        return String.join(",", services);
    }
}
