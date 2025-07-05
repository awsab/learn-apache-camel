package com.me.learning.consul.soapservice;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

@WebService (name = "GreetingService", targetNamespace = "http://soapservice.consul.learning.me.com/")
public interface GreetingService {

    @WebMethod
    @WebResult (name = "soapServiceRequest", targetNamespace = "http://soapservice.consul.learning.me.com/")
    SoapServiceResponse greet(@WebParam (name = "soapServiceRequest", targetNamespace = "http://soapservice.consul.learning.me.com/") SoapServiceRequest soapServiceRequest);
}
