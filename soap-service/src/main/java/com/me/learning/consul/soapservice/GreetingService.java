package com.me.learning.consul.soapservice;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

@WebService (name = "GreetingService", targetNamespace = "http://soapservice.consul.learning.me.com/")
public interface GreetingService {

    @WebMethod
    @WebResult (name = "greeting", targetNamespace = "http://soapservice.consul.learning.me.com/")
    String greet(@WebParam (name = "name", targetNamespace = "http://soapservice.consul.learning.me.com/") String name);
}
