/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.springinteg.configOld;

import com.me.learning.consul.springinteg.entity.PersonResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.messaging.MessageChannel;

import java.util.Map;

//@Configuration
public class PersonIntegrationConfig {

    @Bean
    public MessageChannel personRequestChannel () {
        return new DirectChannel ();
    }

    @Bean
    public HttpRequestExecutingMessageHandler personOutBoundHandler(){
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler ("http://localhost:8091/person/{personId}");
        handler.setHttpMethod (org.springframework.http.HttpMethod.GET);
        ExpressionParser parser = new SpelExpressionParser ();
        handler.setUriVariableExpressions (Map.of ("personId", parser.parseExpression ("headers['personId']")));
        handler.setLoggingEnabled (true);
        handler.setExpectedResponseType (PersonResponse.class);
        return handler;
    }

    @Bean
    public IntegrationFlow personRequestFlow () {
        return IntegrationFlow.from (personRequestChannel ())
                .enrichHeaders (header -> header.headerExpression ("personId", "payload"))
                .log (message -> "Headers : " + message.getHeaders () + " Payload : " + message.getPayload ())
                .handle (personOutBoundHandler ())
                .get ();
    }
}

