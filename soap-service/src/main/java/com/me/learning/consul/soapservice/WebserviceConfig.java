/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 02/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.soapservice;


import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebserviceConfig {

    @Bean
    public Endpoint endpoint(Bus bus, GreetingServiceImpl greetingService) {
        Endpoint endpoint = Endpoint.create(greetingService);
        endpoint.publish("/greeting");
        // Optionally, you can set the address for the endpoint
        // endpoint.setAddress("/greeting");
        return endpoint;
    }
}
