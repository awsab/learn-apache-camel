/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.springinteg.configOld;

import com.me.learning.consul.springinteg.entity.AddressResponse;
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
public class AddressIntegrationConfig {

    @Bean
    public MessageChannel addressRequestChannel () {
        return new DirectChannel ();
    }

    @Bean
    HttpRequestExecutingMessageHandler addressOutboundHandler () {
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler ("http://localhost:8090/address/{addressId}");
        handler.setHttpMethod (HttpMethod.GET);
        ExpressionParser parser = new SpelExpressionParser ();
        handler.setUriVariableExpressions(Map.of("addressId",  parser.parseExpression ("headers['addressId']")));
        handler.setLoggingEnabled (true);
        handler.setExpectedResponseType (AddressResponse.class);
        return handler;
    }

    @Bean
    public IntegrationFlow addressRequestFlow () {
        return IntegrationFlow.from (addressRequestChannel ())
                .enrichHeaders ( header -> header.headerExpression ("addressId", "payload"))
                .log (message -> "Headers : " + message.getHeaders () + " Payload : " + message.getPayload ())
                .handle (addressOutboundHandler ())
                .get ();
    }

}
