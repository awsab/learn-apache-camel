package com.me.learning.consul.springinteg.config;

import com.me.learning.consul.springinteg.entity.AddressResponse;
import com.me.learning.consul.springinteg.entity.PersonResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.messaging.MessageChannel;

import java.util.Map;

@Configuration
public class CustomHttpServiceConfigAdv {

    private final ExpressionParser expressionParser;

    public CustomHttpServiceConfigAdv(ExpressionParser expressionParser) {
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
    public IntegrationFlow addressRequestFlow() {
        return createServiceFlow(
                "addressRequestChannel",
                "http://localhost:8090/address/{addressId}",
                "addressId",
                AddressResponse.class
        );
    }

    @Bean
    public IntegrationFlow personRequestFlow() {
        return createServiceFlow(
                "personRequestChannel",
                "http://localhost:8091/person/{personId}",
                "personId",
                PersonResponse.class
        );
    }

    private IntegrationFlow createServiceFlow(String channelName, String serviceUrl, String headerName, Class<?> responseType) {
        return IntegrationFlow.from(channelName)
                .enrichHeaders(header -> header.headerExpression(headerName, "payload"))
                .log(message -> "Headers: " + message.getHeaders() + " Payload: " + message.getPayload())
                .handle(createHttpRequestHandler(serviceUrl, headerName, responseType))
                .get();
    }

    private HttpRequestExecutingMessageHandler createHttpRequestHandler(String serviceUrl, String headerName, Class<?> responseType) {

            HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler(serviceUrl);
            handler.setHttpMethod( HttpMethod.GET);
            handler.setUriVariableExpressions( Map.of(headerName,
                    expressionParser.parseExpression("headers['" + headerName + "']")));
            handler.setLoggingEnabled(true);
            handler.setExpectedResponseType(responseType);
            return handler;

    }
}
