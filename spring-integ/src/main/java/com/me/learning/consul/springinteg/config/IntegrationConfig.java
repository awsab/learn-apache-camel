/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.springinteg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class IntegrationConfig {

    @Value ("${rest.services.endpoint1}")
    private String endpoint1;

    @Bean
    public IntegrationFlow restServiceFlow () {
        return IntegrationFlow.from (restRequestChannel ())
                .handle (outboundHandler ())
                .channel (responseChannel ())
                .get ();
    }

    @Bean
    public MessageChannel restRequestChannel () {
        return new DirectChannel ();
    }

    @Bean
    public HttpRequestExecutingMessageHandler outboundHandler () {
        HttpRequestExecutingMessageHandler handler =
                new HttpRequestExecutingMessageHandler (endpoint1);
        handler.setHttpMethod (HttpMethod.GET);
        handler.setExpectedResponseType (String.class);
        return handler;
    }

    @Bean
    public MessageChannel responseChannel () {
        return new DirectChannel ();
    }

    @Bean
    public IntegrationFlow responseLoggerFlow () {
        return IntegrationFlow.from (responseChannel ())
                .handle (msg -> System.out.println ("Response: " + msg.getPayload ()))
                .get ();
    }

}
