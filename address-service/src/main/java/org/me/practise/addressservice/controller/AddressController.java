package org.me.practise.addressservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.me.practise.addressservice.entity.Address;
import org.me.practise.addressservice.entity.AggregationRequest;
import org.me.practise.addressservice.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
@Slf4j
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/all")
    public Address getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @PostMapping()
    public ResponseEntity<Address> addAddress(@RequestBody AggregationRequest request) {
        log.info("Received request to add address: {}", request);
        Address address = new Address("123 Main St", "Springfield", "IL", "62701");
        return ResponseEntity.ok (address);
    }
}
