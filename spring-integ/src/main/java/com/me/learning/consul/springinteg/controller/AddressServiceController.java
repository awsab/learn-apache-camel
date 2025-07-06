/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.springinteg.controller;

import com.me.learning.consul.springinteg.entity.AddressResponse;
import com.me.learning.consul.springinteg.service.AddressService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/address-controller")
public class AddressServiceController {

    private final AddressService addressService;

    public AddressServiceController (AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping ("/{addressId}")
    public AddressResponse getAddressById (@PathVariable Long addressId) {
        return addressService.findAddressById (addressId);
    }
}
