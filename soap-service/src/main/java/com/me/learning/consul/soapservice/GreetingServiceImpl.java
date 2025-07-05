/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 02/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.soapservice;

import jakarta.jws.WebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@WebService (endpointInterface = "com.me.learning.consul.soapservice.GreetingService")
@Service
@Slf4j
public class GreetingServiceImpl implements GreetingService{
    @Override
    public SoapServiceResponse greet (SoapServiceRequest soapServiceRequest) {
        log.info ("Received request fro SOAP service : {}", soapServiceRequest.toString ());

        return new SoapServiceResponse (
                soapServiceRequest.getServiceNames (),
                soapServiceRequest.getCorrelationId (),
                soapServiceRequest.getUserId ());

    }
}
