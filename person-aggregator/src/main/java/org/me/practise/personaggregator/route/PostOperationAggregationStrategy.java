/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 01/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package org.me.practise.personaggregator.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.learning.consul.soapservice.SoapServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.cxf.message.MessageContentsList;
import org.me.practise.personaggregator.entity.PersonAggregatedResponse;
import org.me.practise.personaggregator.entity.SoapAccount;

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

                if(newBody instanceof MessageContentsList soapResponse) {
                    if(!soapResponse.isEmpty ()) {
                        Object firstElement = soapResponse.get (0);
                        if (firstElement instanceof SoapServiceResponse soapServiceResponse) {
                            personAggregatedResponse.setSoapAccount (
                                    new SoapAccount (
                                            soapServiceResponse.getServiceNames (),
                                            soapServiceResponse.getCorrelationId (),
                                            soapServiceResponse.getUserId ()
                                    )
                            );
                        }else {
                            log.warn ("Unexpected type in SOAP response: {}", firstElement.getClass ().getSimpleName ());
                        }
                    } else {
                        log.warn ("SOAP response is empty for service: {}", serviceName);
                    }

                }else {
                    String className = newBody.getClass ().getSimpleName ();
                    String methodName = "set" + className;

                    var methodSetter = PersonAggregatedResponse.class.getMethod (methodName, newBody.getClass ());
                    methodSetter.invoke (personAggregatedResponse, newBody);
                }
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
            }else if ("soap-service-param".equals (serviceName)) {
                personAggregatedResponse.setSoapServiceError (errorMessage);
            }
        } else {
            log.warn ("Received empty body from service: {}", serviceName);
        }

        Exchange resultExchange = oldExchange != null ? oldExchange : newExchange;
        resultExchange.getIn ().setBody (personAggregatedResponse);
        return resultExchange;
    }
}
