package org.me.practise.personaggregator.route;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.camel.Exchange;

public class RequestParamAggregationStrategy implements org.apache.camel.AggregationStrategy {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        try {
            String newBodyStr = newExchange.getIn().getBody(String.class);
            if (newBodyStr == null || newBodyStr.trim().isEmpty()) {
                newBodyStr = "{}";
            }
            JsonNode newBody = mapper.readTree(newBodyStr);

            ObjectNode combined;
            if (oldExchange == null) {
                combined = mapper.createObjectNode();
            } else {
                String oldBodyStr = oldExchange.getIn().getBody(String.class);
                if (oldBodyStr == null || oldBodyStr.trim().isEmpty()) {
                    oldBodyStr = "{}";
                }
                combined = mapper.readTree(oldBodyStr).deepCopy();
            }

            String key = newExchange.getIn().getHeader("serviceName", String.class);
            if (key == null || key.isEmpty()) key = "unknown";
            combined.set(key, newBody);

            newExchange.getIn().setBody(combined.toString());
            return newExchange;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error aggregating: " + e.getMessage(), e);
        }
    }
}
