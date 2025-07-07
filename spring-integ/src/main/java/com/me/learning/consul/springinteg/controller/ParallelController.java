package com.me.learning.consul.springinteg.controller;

import com.me.learning.consul.springinteg.entity.AddressResponse;
import com.me.learning.consul.springinteg.entity.PersonAddressResponse;
import com.me.learning.consul.springinteg.entity.PersonResponse;
import com.me.learning.consul.springinteg.gateway.IAddressGateway;
import com.me.learning.consul.springinteg.gateway.IPersonGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/parallel-controller")
public class ParallelController {

    private final IAddressGateway addressGateway;
    private final IPersonGateway personGateway;

    public ParallelController(IAddressGateway addressGateway, IPersonGateway personGateway) {
        this.addressGateway = addressGateway;
        this.personGateway = personGateway;
    }

    @GetMapping("/address/{addressId}/person/{personId}")
    public CompletableFuture<?> getAddressAndPersonById(@PathVariable Long addressId, @PathVariable Long personId) {
        CompletableFuture<AddressResponse> addressFuture = addressGateway.getAddressByIdAsync(addressId);
        CompletableFuture<PersonResponse> personFuture = personGateway.getPersonByIdAsync(personId);

        /*return CompletableFuture.allOf(addressFuture, personFuture)
                .thenApply(v -> {
                    Object addressResponse = addressFuture.join();
                    Object personResponse = personFuture.join();
                    return new Object[]{addressResponse, personResponse};
                });*/

        return CompletableFuture.allOf(addressFuture, personFuture)
                .thenApply ( result -> new PersonAddressResponse (
                                                personFuture.join (),
                                                addressFuture.join ()) );
    }
}
