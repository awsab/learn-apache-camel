package org.me.practise.personaggregator.route;

import com.me.learning.consul.soapservice.SoapServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.cxf.message.MessageContentsList;
import org.me.practise.personaggregator.entity.PersonAggregatedResponse;
import org.me.practise.personaggregator.entity.SoapAccount;


import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RefactoredAggregationStrategy implements AggregationStrategy {

    private final Map<Class<?>, ResponseHandler> responseHandlers;
    private final Map<String, ErrorHandler> errorHandlers;

    public RefactoredAggregationStrategy() {
        this.responseHandlers = initResponseHandlers();
        this.errorHandlers = initErrorHandlers();
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        PersonAggregatedResponse response = initializeResponse(oldExchange);
        String serviceName = newExchange.getIn().getHeader("serviceName", String.class);
        String errorMessage = newExchange.getIn().getHeader("serviceError", String.class);
        Object newBody = newExchange.getIn().getBody();

        try {
            if (newBody != null) {
                handleResponse(response, newBody);
            } else if (errorMessage != null) {
                handleError(response, serviceName, errorMessage);
            } else {
                log.warn("Received empty body from service: {}", serviceName);
            }
        } catch (Exception e) {
            log.error("Error processing response: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return prepareResultExchange(oldExchange, newExchange, response);
    }

    private Map<Class<?>, ResponseHandler> initResponseHandlers() {
        Map<Class<?>, ResponseHandler> handlers = new HashMap<>();

        handlers.put(MessageContentsList.class, (response, body) -> {
            MessageContentsList soapResponse = (MessageContentsList) body;
            if (!soapResponse.isEmpty() && soapResponse.getFirst () instanceof SoapServiceResponse soapServiceResponse) {
                response.setSoapAccount(new SoapAccount(
                        soapServiceResponse.getServiceNames(),
                        soapServiceResponse.getCorrelationId(),
                        soapServiceResponse.getUserId()
                ));
            }
        });

        // Generic handler for other types using reflection
        handlers.put(Object.class, (response, body) -> {
            String className = body.getClass().getSimpleName();
            String methodName = "set" + className;
            var methodSetter = PersonAggregatedResponse.class.getMethod(methodName, body.getClass());
            methodSetter.invoke(response, body);
        });

        return handlers;
    }

    private Map<String, ErrorHandler> initErrorHandlers() {
        Map<String, ErrorHandler> handlers = new HashMap<>();
        handlers.put("person-service-param", PersonAggregatedResponse::setPersonServiceError );
        handlers.put("address-service-param", PersonAggregatedResponse::setAddressServiceError );
        handlers.put("soap-service-param", PersonAggregatedResponse::setSoapServiceError );
        return handlers;
    }

    private PersonAggregatedResponse initializeResponse(Exchange oldExchange) {
        return oldExchange == null ?
                new PersonAggregatedResponse() :
                oldExchange.getIn().getBody(PersonAggregatedResponse.class);
    }

    private void handleResponse(PersonAggregatedResponse response, Object body) throws Exception {
        ResponseHandler handler = responseHandlers.entrySet().stream()
                                                     .filter(entry -> entry.getKey().isInstance(body))
                                                     .findFirst()
                                                     .map(Map.Entry::getValue)
                                                     .orElse(responseHandlers.get(Object.class));

        handler.handle(response, body);
    }

    private void handleError(PersonAggregatedResponse response, String serviceName, String errorMessage) {
        ErrorHandler handler = errorHandlers.get(serviceName);
        if (handler != null) {
            handler.handle(response, errorMessage);
        } else {
            log.warn("No error handler found for service: {}", serviceName);
        }
    }

    private Exchange prepareResultExchange(Exchange oldExchange, Exchange newExchange, PersonAggregatedResponse response) {
        Exchange resultExchange = oldExchange != null ? oldExchange : newExchange;
        resultExchange.getIn().setBody(response);
        return resultExchange;
    }

    @FunctionalInterface
    private interface ResponseHandler {
        void handle(PersonAggregatedResponse response, Object body) throws Exception;
    }

    @FunctionalInterface
    private interface ErrorHandler {
        void handle(PersonAggregatedResponse response, String errorMessage);
    }
}

