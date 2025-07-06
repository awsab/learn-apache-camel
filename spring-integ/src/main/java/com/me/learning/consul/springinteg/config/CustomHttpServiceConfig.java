package com.me.learning.consul.springinteg.config;

import com.me.learning.consul.springinteg.entity.AddressResponse;
import com.me.learning.consul.springinteg.entity.PersonResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.messaging.MessageChannel;

import java.util.Map;

//@Configuration
public class CustomHttpServiceConfig {

    private final ExpressionParser expressionParser;

    public CustomHttpServiceConfig(ExpressionParser expressionParser) {
        this.expressionParser = new SpelExpressionParser ();
    }

    @Bean
    public MessageChannel addressRequestChannel() {
        return new DirectChannel ();
    }

    @Bean
    public MessageChannel personRequestChannel() {
        return new DirectChannel();
    }

    @Bean
    public HttpRequestExecutingMessageHandler addressOutboundHandler() {
        return createHttpRequestHandler(
                "http://localhost:8090/address/{addressId}",
                "addressId",
                AddressResponse.class
        );
    }

    @Bean
    public HttpRequestExecutingMessageHandler personOutboundHandler() {
        return createHttpRequestHandler(
                "http://localhost:8090/person/{personId}",
                "personId",
                PersonResponse.class
        );
    }

    @Bean
    public IntegrationFlow addressRequestFlow() {
        return createGenericIntegrationFlow(
                "addressRequestChannel",
                "addressId",
                addressOutboundHandler()
        );
    }

    @Bean
    public IntegrationFlow personRequestFlow() {
        return createGenericIntegrationFlow(
                "personRequestChannel",
                "personId",
                personOutboundHandler()
        );
    }

    private HttpRequestExecutingMessageHandler createHttpRequestHandler(
            String channelName, String headerName, Class<?> responseType) {
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler(channelName);
        handler.setHttpMethod( HttpMethod.GET);
        handler.setUriVariableExpressions(Map.of(headerName, expressionParser.parseExpression("headers['" + headerName + "']")));
        handler.setExpectedResponseType(responseType);
        return handler;
    }

    private IntegrationFlow createGenericIntegrationFlow(String channelName, String headerName, HttpRequestExecutingMessageHandler handler) {
        return IntegrationFlow.from(channelName)
                              .enrichHeaders(header -> header.headerExpression(headerName, "payload"))
                              .log(message -> "Headers: " + message.getHeaders() + " Payload: " + message.getPayload())
                              .handle(handler)
                              .get();
    }
}
