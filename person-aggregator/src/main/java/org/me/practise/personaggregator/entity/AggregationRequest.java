package org.me.practise.personaggregator.entity;

import lombok.Data;

import java.util.List;

@Data
public class AggregationRequest {
    private List<String> serviceNames;
    private String correlationId;
    private String userId;

    public AggregationRequest() {
    }
}
