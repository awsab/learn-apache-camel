/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 01/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package org.me.practise.personaggregator.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.me.practise.personaggregator.entity.Address;
import org.me.practise.personaggregator.entity.Person;
import org.me.practise.personaggregator.entity.PersonAggregatedResponse;

@Slf4j
public class PostOperationAggregationStrategy implements org.apache.camel.AggregationStrategy {

    ObjectMapper mapper = new ObjectMapper ();
    PersonAggregatedResponse personAggregatedResponse;

    @Override
    public Exchange aggregate (Exchange oldExchange, Exchange newExchange) throws RuntimeException {
        if ( oldExchange == null ) {
            personAggregatedResponse = new PersonAggregatedResponse ();
        } else {
            personAggregatedResponse = oldExchange.getIn ().getBody (PersonAggregatedResponse.class);
        }

        String serviceName = newExchange.getIn ().getHeader ( "serviceName", String.class );
        String errorMessage = newExchange.getIn ().getHeader ( "serviceError", String.class );

        Object newBody = newExchange.getIn ().getBody ();
        log.info ("New Body Received: {}", newBody);

        if ( newBody != null ) {

            try {
                String className = newBody.getClass ().getSimpleName ();
                String methodName = "set" + className;

                var methodSetter = PersonAggregatedResponse.class.getMethod (methodName, newBody.getClass ());
                methodSetter.invoke (personAggregatedResponse, newBody);
            } catch (Exception e) {
                log.error ("Error processing body: {} - {}", newBody, e.getMessage ());
                throw new RuntimeException (e);
            }
        }else if(errorMessage != null) {
            log.error ("Error response received from service: {}", serviceName);
            if ("person-service-param".equals (serviceName)) {
                personAggregatedResponse.setPersonServiceError (errorMessage);
            } else if ("address-service-param".equals (serviceName)) {
                personAggregatedResponse.setAddressServiceError (errorMessage);
            }
        } else {
            log.warn ("Received empty body from service: {}", serviceName);
        }

        Exchange resultExchange = oldExchange != null ? oldExchange : newExchange;
        resultExchange.getIn ().setBody (personAggregatedResponse);
        return resultExchange;
    }
}
