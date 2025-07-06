/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.addressservicerequest.controller;

import com.me.learning.consul.addressservicerequest.entity.AddressRequest;
import com.me.learning.consul.addressservicerequest.entity.AddressResponse;
import com.me.learning.consul.addressservicerequest.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping ("/address")
@RestController
@Slf4j
public class AddressController {

    private final AddressService addressService;

    public AddressController (AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping ("/{addressId}")
    public AddressResponse getAddressById (@PathVariable Long addressId) {
        log.info ("Address request received with Body : {}", addressId);
        return addressService.getAddressById (addressId);
    }

}
