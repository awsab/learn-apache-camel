package org.me.practise.personaggregator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.me.practise.personaggregator.entity.AggregationRequest;
import org.me.practise.personaggregator.entity.PersonAggregatedResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/aggregate")
@RequiredArgsConstructor
@Slf4j
public class AggregateController {

    private final ProducerTemplate producerTemplate;

    @GetMapping
    public PersonAggregatedResponse aggregate() {
        PersonAggregatedResponse responseBody = producerTemplate.requestBody ( "direct:aggregate", null, PersonAggregatedResponse.class );
        log.info ( "Response from aggregation: {}", responseBody );
        return responseBody;
    }

    @GetMapping("/withObject")
    public PersonAggregatedResponse aggregateWithObject() {
        AggregationRequest request = new AggregationRequest();
        request.setCorrelationId("12345");
        request.setUserId("user-123");
        request.setServiceNames(List.of("person-service", "address-service", "soap-service"));

        log.info("Requesting aggregation with object: {}", request);
        PersonAggregatedResponse responseBody = producerTemplate.requestBody ( "direct:aggregateWithObject", request, PersonAggregatedResponse.class );
        log.info ( "Response from aggregation with object: {}", responseBody );
        return responseBody;
    }

    @PostMapping
    public PersonAggregatedResponse createPersonAggregation(AggregationRequest request) {
        log.info("Creating person aggregation with request: {}", request);
        Map<String, Object> headers = new HashMap<> ();
        headers.put("correlationId", UUID.randomUUID().toString());

        PersonAggregatedResponse responseBody = producerTemplate.requestBodyAndHeaders ("direct:person-aggregate", request, headers, PersonAggregatedResponse.class);
        log.info("Response from person aggregation: {}", responseBody);
        return responseBody;
    }
}
