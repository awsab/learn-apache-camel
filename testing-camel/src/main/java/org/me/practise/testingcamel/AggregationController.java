package org.me.practise.testingcamel;

import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/aggregate")
class AggregationController {

    private final ProducerTemplate producerTemplate;

    AggregationController(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @GetMapping("/withObject")
    public String aggregateWithObject() {
        AggregationRequest request = new AggregationRequest();
        request.setCorrelationId("12345");
        request.setUserId("user-123");
        request.setServiceNames( List.of("direct:person-service", "direct:address-service"));

        return producerTemplate.requestBody("direct:aggregateWithObject", request, String.class);
    }
}
