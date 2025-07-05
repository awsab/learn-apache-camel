/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 02/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package org.me.practise.personaggregator;



import org.apache.camel.component.cxf.jaxws.CxfEndpoint;
import org.apache.cxf.Bus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfClientConfig {

    @Bean
    public CxfEndpoint greetingClient(Bus bus){
        CxfEndpoint cxfEndpoint = new CxfEndpoint();
        cxfEndpoint.setBus(bus);
        cxfEndpoint.setAddress("http://localhost:8080/soap-service/greeting");
        try {
            cxfEndpoint.setServiceClass("com.me.learning.consul.soapservice.GreetingService");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException (e);
        }
        cxfEndpoint.setBus (bus);
        return cxfEndpoint;
    }
}
