/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 02/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.soapservice;

import jakarta.jws.WebService;
import org.springframework.stereotype.Service;

@WebService (endpointInterface = "com.me.learning.consul.soapservice.GreetingService")
@Service
public class GreetingServiceImpl implements GreetingService{
    @Override
    public String greet (String name) {
        return "Hello, " + name + "! Welcome to the SOAP service.";
    }
}
