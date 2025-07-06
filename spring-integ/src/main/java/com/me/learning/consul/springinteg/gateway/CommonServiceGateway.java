package com.me.learning.consul.springinteg.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway(
        name = "commonServiceGateway",
        defaultRequestTimeout = "5000",
        defaultReplyTimeout = "5000")
public interface CommonServiceGateway {

    @Gateway(requestChannel = "addressRequestChannel")
    <T> T getById(@Payload Long id);

    @Gateway(requestChannel = "personRequestChannel")
    <T> T getAddressById(@Payload Long addressId);

    @Gateway(requestChannel = "personRequestChannel")
    <T> T getPersonById(@Payload Long personId);

    @Gateway(requestChannel = "#{#serviceType + 'RequestChannel'}")
    <T> T getByServiceType(@Payload Object payload, @Header("serviceType") String serviceType);

    @Gateway(requestChannel = "#{#serviceType + 'RequestChannel'}", replyTimeout = 10000)
    <T> T getByServiceTypeWithTimeout(@Payload Object payload, @Header("serviceType") String serviceType);
}

