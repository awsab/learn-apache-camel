package org.me.practise.personaggregator.route;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.me.practise.personaggregator.entity.AggregationRequest;
import org.me.practise.personaggregator.utils.FallbackResponse;
import org.me.practise.personaggregator.utils.RetryProperties;
import org.me.practise.personaggregator.utils.ServiceResolver;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

@Component
public class AggregateRoute extends RouteBuilder {

    private final RetryProperties retryProperties;

    public AggregateRoute(RetryProperties retryProperties) {
        this.retryProperties = retryProperties;
    }

    @Override
    public void configure() {

        /*onException ( ConnectException.class, SocketTimeoutException.class )
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
                } );*/

        from("direct:aggregateWithObject")
                .routeId("aggregateWithObject")
                .process(exchange -> {
                    AggregationRequest request = exchange.getIn().getBody(AggregationRequest.class);
                    if (request != null) {
                        exchange.setProperty("aggregationRequest", request); // ✅ the actual POJO
                        exchange.setProperty("correlationId", request.getCorrelationId());
                        exchange.setProperty("userId", request.getUserId());
                        exchange.setProperty("serviceNames", request.getServiceNames());
                    }
                })
                // 🔥 Convert POJO to JSON (string)
                .setBody(exchange -> exchange.getProperty("aggregationRequest"))
                .marshal().json(JsonLibrary.Jackson)
                //.setHeader("Content-Type", constant("application/json"))

                //.setHeader("correlationId", simple("${exchangeProperty.correlationId}"))
                //.setHeader("userId", simple("${exchangeProperty.userId}"))
                .setHeader("recipientList", method(ServiceResolver.class, "resolveServiceUrl(${exchangeProperty.serviceNames})"))
                .log("📦 recipientList: ${header.recipientList}")
                .recipientList(header("recipientList"))
                .parallelProcessing()
                .streaming ()
                .stopOnException()
                .aggregationStrategy(new RequestParamAggregationStrategy ())
                //.stopOnException ()
                .end()
                .marshal().json()
                .log("✅ Aggregated Result: ${body}");

        from("direct:person-service")
                .routeId("person-service")
                .log("➡️ Reached person-service route")

                .doTry()
                    //.log("🔁 AggregationRequest class: ${exchangeProperty.aggregationRequest.class}")
                    //.setBody(exchange -> exchange.getProperty("aggregationRequest", AggregationRequest.class))
                    //.log("🧪 AggregationRequest to be marshalled: ${body}")

                    //.marshal().json(JsonLibrary.Jackson)
                    //.log("📤 Marshalled JSON: ${body}")

                    .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))

                    .toD(retryProperties.getPersonService().getUrl() +
                            "?bridgeEndpoint=true&throwExceptionOnFailure=true")

                    .unmarshal().json(JsonLibrary.Jackson)
                    .marshal().json(JsonLibrary.Jackson)
                    .convertBodyTo(String.class)
                    .log("✅ person-service response: ${body}")

                .doCatch(Exception.class)
                .log("❌ Exception caught in person-service: ${exception.message}")
                .process(fallbackProcessor())
                .end();


        from ( "direct:address-service" )
                .routeId ( "address-service" )
                .doTry ()
                    //.log("🔁 AggregationRequest class: ${exchangeProperty.aggregationRequest.class}")
                    //.setBody (exchange -> exchange.getProperty ( "aggregationRequest", AggregationRequest.class ))

                    //.marshal ().json (JsonLibrary.Jackson)
                    //.log("📤 JSON sent to address-service: ${body}")

                    .setHeader ( Exchange.HTTP_METHOD, constant ( "POST" ) )
                    .setHeader ( Exchange.CONTENT_TYPE, constant ( "application/json" ) )

                    .toD ( retryProperties.getAddressService ().getUrl () + "?bridgeEndpoint=true&throwExceptionOnFailure=true" )
                    //.convertBodyTo ( String.class )
                    //.removeHeaders ( "*" )
                    //.setHeader ( "serviceName", constant ( "address-service" ) )

                    .unmarshal ().json ( JsonLibrary.Jackson )
                    .marshal ().json ( JsonLibrary.Jackson )
                    .convertBodyTo ( String.class )
                    .log ( "Response from address-service: ${body}" )

                .doCatch ( Exception.class )
                .log ( "❌ Exception caught in address-service: ${exception.message}" )
                .process ( fallbackProcessor() )
                .end();

    }

    private Processor fallbackProcessor() {
        return exchange -> {
            String serviceName = exchange.getIn().getHeader("serviceName", String.class);
            Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
            Map<String, Object> fallbackResponse = new FallbackResponse().getFallbackResponse(serviceName, exception);
            exchange.getIn().setBody(fallbackResponse);
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        };
    }
}
