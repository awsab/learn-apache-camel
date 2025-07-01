package org.me.practise.testingcamel;

import java.util.List;

public class AggregationRequest {

    private String correlationId;
    private String userId;
    private List<String> serviceNames;
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<String> getServiceNames() { return serviceNames; }
    public void setServiceNames(List<String> serviceNames) { this.serviceNames = serviceNames; }

}
