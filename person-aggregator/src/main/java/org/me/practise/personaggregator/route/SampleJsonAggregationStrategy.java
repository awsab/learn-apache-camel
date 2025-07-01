package org.me.practise.personaggregator.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.me.practise.personaggregator.entity.Address;
import org.me.practise.personaggregator.entity.Person;
import org.me.practise.personaggregator.entity.PersonAggregatedResponse;

@Slf4j
public class SampleJsonAggregationStrategy implements AggregationStrategy {

    private final ObjectMapper mapper = new ObjectMapper ();
    /*
        This creates a single shared instance of PersonAggregatedResponse, so all requests will modify the same object, leading to:
            •	Cross-request data leaks
	        •	Thread safety issues
	        •	Stale values across calls
	 */
    //private final PersonAggregatedResponse personAggregatedResponse = new PersonAggregatedResponse ();

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        PersonAggregatedResponse personAggregatedResponse;

        if (oldExchange == null) {
            // This is the first response, so we create a new PersonAggregatedResponse
            personAggregatedResponse = new PersonAggregatedResponse ();
        } else {
            // We already have a response, so we use the existing one
            personAggregatedResponse = oldExchange.getIn ().getBody ( PersonAggregatedResponse.class );
        }

        try {

            String serviceName = newExchange.getIn ().getHeader ( "serviceName", String.class );
            String newBody = newExchange.getIn ().getBody ( String.class );
            if( newBody == null || newBody.trim ().isEmpty() ) {
                log.warn ( "Received empty body from service: {}", serviceName );
                newBody = "{}"; // Default to empty JSON object if body is null or empty
            }
            JsonNode node = mapper.readTree ( newBody );

            if (newBody.contains ( "error" )) {
                log.error ( "Error response received from service: {}", serviceName );

                String errorMessage = node.fields ().next ().getValue ().asText ();
                if ("person-service".equals ( serviceName )) {
                    log.error ( "Person service error: {}", errorMessage );
                    personAggregatedResponse.setPersonServiceError ( errorMessage );
                } else if ("address-service".equals ( serviceName )) {
                    log.error ( "Address service error: {}", errorMessage );
                    personAggregatedResponse.setAddressServiceError ( errorMessage );
                }
            } else {
                if ("person-service".equals ( serviceName )) {
                    log.info ( "Received person data from service: {}", serviceName );
                    personAggregatedResponse.setPerson ( mapper.treeToValue ( node, Person.class ) );
                } else if ("address-service".equals ( serviceName )) {
                    log.info ( "Received address data from service: {}", serviceName );
                    personAggregatedResponse.setAddress ( mapper.treeToValue ( node, Address.class ) );
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }

        /*if(oldExchange == null) {
            // This is the first response, so we just set the body
            newExchange.getIn().setBody(personAggregatedResponse);
            return newExchange;
        } else {
            // We already have a response, so we merge the new data into it
            oldExchange.getIn().setBody(personAggregatedResponse);
            return oldExchange;
        }*/
        newExchange.getIn ().setBody ( personAggregatedResponse );
        return newExchange;
    }
}
