package org.me.practise.personaggregator.route;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.me.practise.personaggregator.entity.Address;
import org.me.practise.personaggregator.entity.Person;
import org.me.practise.personaggregator.entity.PersonAggregatedResponse;

import java.util.function.Consumer;

@Slf4j
public class JsonAggregationStrategy implements org.apache.camel.AggregationStrategy {

    private final ObjectMapper mapper = new ObjectMapper ();
    //private final PersonAggregatedResponse personAggregatedResponse = new PersonAggregatedResponse ();

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        // This is the service name that will be used as the key in the aggregated JSON object
        /*
        try {


            String serviceName = newExchange.getIn().getHeader("serviceName", String.class);
            String newBody = newExchange.getIn().getBody(String.class);
            JsonNode newNode = mapper.readTree(newBody);

            ObjectNode aggregated;

            if(oldExchange == null) {
                aggregated = mapper.createObjectNode();
                aggregated.set(serviceName, newNode);
                newExchange.getIn().setBody(mapper.writeValueAsString(aggregated));
                return newExchange;
            } else {
                String oldBody = oldExchange.getIn().getBody(String.class);
                aggregated = mapper.readValue(oldBody, ObjectNode.class);
                aggregated.set(serviceName, newNode);
                oldExchange.getIn().setBody(mapper.writeValueAsString(aggregated));
                return oldExchange;
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }
        */

        /*try {

            String newBody = newExchange.getIn().getBody(String.class);
            JsonNode newNode = mapper.readTree(newBody);

            ObjectNode aggregated;

            if (oldExchange == null) {
                aggregated = mapper.createObjectNode();

                if(newNode.isObject()) {
                    aggregated.setAll((ObjectNode) newNode);
                } else {
                    aggregated.set("person-service", newNode);
                }

                newExchange.getIn().setBody(mapper.writeValueAsString(aggregated));
                return newExchange;
            } else {
                String oldBody = oldExchange.getIn().getBody(String.class);
                aggregated = (ObjectNode) mapper.readTree ( oldBody );

                if(newNode.isObject()) {
                    aggregated.setAll((ObjectNode) newNode);
                } else {
                    aggregated.set("address-service", newNode);
                }

                oldExchange.getIn().setBody(mapper.writeValueAsString(aggregated));
                return oldExchange;
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }*/

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
            String body = newExchange.getIn ().getBody ( String.class );

            log.info ( "Received response from serviceName: {}", serviceName );
            log.info ( "Response header: {}", serviceName );


            switch (serviceName) {
                case "person-service":
                    handleServiceResponse (
                            "Person-service",
                            body,
                            Person.class,
                            personAggregatedResponse::setPerson,
                            personAggregatedResponse::setPersonServiceError
                    );
                    break;
                case "address-service":
                    handleServiceResponse (
                            "address-service",
                            body,
                            Address.class,
                            personAggregatedResponse::setAddress,
                            personAggregatedResponse::setAddressServiceError
                    );
                    break;
                default:
                    String errorMessage = String.format ( "Unknown service: %s", serviceName );
                    log.error ( errorMessage );

                    personAggregatedResponse.setPersonServiceError ( errorMessage );
                    personAggregatedResponse.setAddressServiceError ( errorMessage );
            }


            /*if("person-service".equals(serviceName)) {
                try {
                    Person person = mapper.readValue(body, Person.class);
                    log.info("Person Service Response: {}", person);
                    personAggregatedResponse.setPerson(person);
                }catch (Exception e) {
                    personAggregatedResponse.setPersonServiceError ( e.getMessage() );
                }
            } else if("address-service".equals(serviceName)) {
                try {
                    Address address = mapper.readValue(body, Address.class);
                    log.info("Address Service Response: {}", address);
                    personAggregatedResponse.setAddress(address);
                }catch (Exception e) {
                    personAggregatedResponse.setAddressServiceError ( e.getMessage() );
                }
            } else {
                // Handle error case
                String errorMessage = String.format("Unknown service: %s", serviceName);
                *//*if(routeId.equals("person-service")) {
                    personAggregatedResponse.setPersonServiceError(errorMessage);
                } else if(routeId.equals("address-service")) {
                    personAggregatedResponse.setAddressServiceError(errorMessage);
                }*//*
            }*/

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
        } catch (Exception e) {
            throw new RuntimeException ( e );
        }


    }

    private <T> void handleServiceResponse(String serviceName, String body, Class<T> responseType, Consumer<T> responseConsumer, Consumer<String> errorConsumer) {
        try {
            if (body.contains ( "error" )) {
                JsonNode errorNode = mapper.readTree ( body );
                // fields method on JsonNode is deprecated, use properties instead
                //String errorMessage = errorNode.fields().next().getValue().asText();
                String errorMessage = errorNode.properties ().stream ().iterator ().next ().getValue ().asText ();

                log.error ( "Error in response from {}: {}", serviceName, body );
                errorConsumer.accept ( errorMessage );
            } else {
                T response = mapper.readValue ( body, responseType );
                log.info ( "{} Service Response: {}", serviceName, response );
                responseConsumer.accept ( response );
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage ();
            log.error ( "Error processing {} service response: {}", serviceName, errorMessage );
            errorConsumer.accept ( errorMessage );
        }
    }
}
