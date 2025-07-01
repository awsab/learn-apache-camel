package org.me.practise.personaggregator.exception;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.me.practise.personaggregator.utils.RetryProperties;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

@Component
public class GlobalExceptionHandlerRoute extends RouteBuilder {
    private final RetryProperties retryProperties;

    public GlobalExceptionHandlerRoute(RetryProperties retryProperties) {
        this.retryProperties = retryProperties;
    }

    @Override
    public void configure() {

        onException (Exception.class )
                .maximumRedeliveries ( retryProperties.getMaxAttempts () )
                .redeliveryDelay ( retryProperties.getInitialInterval () )
                .maximumRedeliveryDelay ( retryProperties.getMaxInterval () )
                .retryAttemptedLogLevel ( LoggingLevel.ERROR )
                .handled ( true )
                .log ( "Exception occurred: ${exception.message}" )
                .removeHeader ( "*" )
                .setHeaders ( "Content-Type", constant ( "application/json" ), "serviceName", simple ( "${routeId}" ) )
                .process ( exchange -> {
                    String serviceName = exchange.getIn ().getHeader ( "serviceName", String.class );
                    String errorMessage = exchange.getProperty ( Exchange.EXCEPTION_CAUGHT, Exception.class ).getMessage ();
                    String responseBody = String.format ( "{ \"error\": \"%s\", \"serviceName\": \"%s\" }", errorMessage.replace ( "\"", "," ), serviceName );
                    exchange.getIn ().setBody ( responseBody );
                    exchange.getIn ().setHeader ( Exchange.HTTP_RESPONSE_CODE, 200 );
                } );
    }
}
