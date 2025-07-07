package com.me.learning.consul.springinteg.gateway;

import com.me.learning.consul.springinteg.entity.AddressRequest;
import com.me.learning.consul.springinteg.entity.AddressResponse;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

@MessagingGateway(
        name = "addressGateway1",
        defaultRequestChannel = "addressRequestChannel",
        defaultRequestTimeout = "5000",
        defaultReplyTimeout = "5000")
public interface IAddressGateway extends BaseServiceGateway<AddressResponse, Long, AddressRequest> {

    AddressResponse getAddressById(Long addressId);

    @Gateway(replyTimeout = 5000)
    AddressResponse getAddressByEmail(String emailId);

    @Async
    public CompletableFuture<AddressResponse> getAddressByIdAsync(Long addressId);
}
