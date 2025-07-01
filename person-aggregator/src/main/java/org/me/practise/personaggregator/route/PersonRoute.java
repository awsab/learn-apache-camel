package org.me.practise.personaggregator.route;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.me.practise.personaggregator.utils.RetryProperties;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.net.SocketTimeoutException;


@Component
public class PersonRoute extends RouteBuilder {

    private final RetryProperties retryProperties;

    public PersonRoute(RetryProperties retryProperties) {
        this.retryProperties = retryProperties;
    }

    @Override
    public void configure() {

//        restConfiguration ().component ( "http" );
//
//        from ("rest:get:/aggregate")
//                .to ( "direct:aggregate" );

        onException ( ConnectException.class, SocketTimeoutException.class )
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

        from ( "direct:aggregate" )
                .multicast ( new JsonAggregationStrategy () )
                .parallelProcessing ( true )
                .to ( "direct:person-service", "direct:address-service" )
                .end ()
                //.removeHeader ( "*" )
                .setHeaders ( "Content-Type", constant ( "application/json" ), "serviceName", constant ( "aggregated-response" ) )
                .log ( "Aggregated Result : ${body}" );

        from ( "direct:person-service" )
                .routeId ( "person-service" )
                .to ( retryProperties.getPersonService ().getUrl () + "?throwExceptionOnFailure=true" )
                .convertBodyTo ( String.class )
                .removeHeaders ( "*" )
                .setHeaders ( "serviceName", constant ( "person-service" ) );

        from ( "direct:address-service" )
                .routeId ( "address-service" )
                .to ( retryProperties.getAddressService ().getUrl () + "?throwExceptionOnFailure=true" )
                .convertBodyTo ( String.class )
                .removeHeaders ( "*" )
                .setHeader ( "serviceName", constant ( "address-service" ) );
    }
}
