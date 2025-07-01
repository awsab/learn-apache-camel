package org.me.practise.testingcamel;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestingCamelApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestingCamelApplication.class, args);
	}

	@Bean
	public RouteBuilder aggregationRoute(ServiceResolver resolver, SampleJsonAggregationStrategy strategy) {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {

				onException(Exception.class)
						.handled(true)
						.log("🔴 Global exception: ${exception.message}")
						.setBody(constant("{}"));

				from("direct:aggregateWithObject")
						.routeId("aggregateWithObject")
						.log("📥 Received AggregationRequest: ${body}")
						.process(exchange -> {
							AggregationRequest request = exchange.getIn().getBody(AggregationRequest.class);
							exchange.setProperty("aggregationRequest", request);
							exchange.setProperty("correlationId", request.getCorrelationId());
							exchange.setProperty("userId", request.getUserId());
							exchange.setProperty("serviceNames", request.getServiceNames());
						})
						.setBody(exchange -> exchange.getProperty("aggregationRequest"))
						.marshal().json( JsonLibrary.Jackson)
						.setHeader("recipientList", method(resolver, "resolveServiceUrl(${exchangeProperty.serviceNames})"))
						.log("📦 Recipient list: ${header.recipientList}")
						.recipientList(header("recipientList"))
						.parallelProcessing()
						.streaming()
						.stopOnException()
						.aggregationStrategy(strategy)
						.end()
						.marshal().json()
						.log("✅ Aggregated result: ${body}");

				from("direct:person-service")
						.routeId("person-service")
						.log("➡️ Reached person-service route")
						.doTry()

						.setHeader( Exchange.HTTP_METHOD, constant("POST"))
						.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
						.toD("http://localhost:8081/api/persons?bridgeEndpoint=true")
						.unmarshal().json(JsonLibrary.Jackson)
						.marshal().json(JsonLibrary.Jackson)
						.convertBodyTo(String.class)
						.log("✅ person-service response: ${body}")
						.doCatch(Exception.class)
						.log("❌ Error in person-service: ${exception.message}")
						.process(fallbackProcessor())
						.end();

				from("direct:address-service")
						.routeId("address-service")
						.log("➡️ Reached address-service route")
						.doTry()

						.setHeader( Exchange.HTTP_METHOD, constant("POST"))
						.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
						.toD("http://localhost:8082/api/address?bridgeEndpoint=true")
						.unmarshal().json(JsonLibrary.Jackson)
						.marshal().json(JsonLibrary.Jackson)
						.convertBodyTo(String.class)
						.log("✅ address-service response: ${body}")
						.doCatch(Exception.class)
						.log("❌ Error in address-service: ${exception.message}")
						.process(fallbackProcessor())
						.end();
			}
		};
	}

	@Bean
	public Processor fallbackProcessor() {
		return exchange -> exchange.getIn().setBody("{\"error\":\"fallback\"}");
	}
}
