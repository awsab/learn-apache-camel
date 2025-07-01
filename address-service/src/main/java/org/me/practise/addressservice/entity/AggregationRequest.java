package org.me.practise.addressservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AggregationRequest {
    private List<String> serviceNames;
    private String correlationId;
    private String userId;

}
