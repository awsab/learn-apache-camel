package com.me.learning.consul.springinteg.controller;

import com.me.learning.consul.springinteg.entity.AddressResponse;
import com.me.learning.consul.springinteg.exception.ResourceNotFoundException;
import com.me.learning.consul.springinteg.gateway.CommonServiceGateway;
import com.me.learning.consul.springinteg.gateway.IAddressGateway;
import com.me.learning.consul.springinteg.gateway.IPersonGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service")
public class ServiceController {

    private final IAddressGateway addressGateway;
    private final IPersonGateway personGateway;
    private final CommonServiceGateway commonServiceGateway;

    public ServiceController(IAddressGateway addressGateway, IPersonGateway personGateway, CommonServiceGateway commonServiceGateway) {
        this.addressGateway = addressGateway;
        this.personGateway = personGateway;
        this.commonServiceGateway = commonServiceGateway;
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<?> getAddressById(@PathVariable Long addressId) {
        AddressResponse addressResponse = addressGateway.getAddressById ( addressId );
        if( addressResponse == null ) {
            throw new ResourceNotFoundException ("Address with ID: " + addressId + " not found");
        }
        return ResponseEntity.ok(addressResponse);
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<?> getPersonById(@PathVariable Long personId) {
        return ResponseEntity.ok(personGateway.getPersonById ( personId ));
    }

    // Common service endpoints
    @PostMapping("/service/{serviceType}")
    public ResponseEntity<?> getByServiceType(@PathVariable String serviceType, @RequestBody Object payload) {
        return ResponseEntity.ok(commonServiceGateway.getByServiceType(payload, serviceType));
    }

    @GetMapping("/service/{serviceType}/{id}")
    public ResponseEntity<?> getByServiceTypeAndId(@PathVariable String serviceType, @PathVariable Long id) {
        Object result = "address".equals(serviceType)
                ? commonServiceGateway.getAddressById(id)
                : commonServiceGateway.getPersonById(id);
        return ResponseEntity.ok(result);
    }
}
