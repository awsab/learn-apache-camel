/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.springinteg.service;

import com.me.learning.consul.springinteg.entity.AddressResponse;
import com.me.learning.consul.springinteg.gateway.AddressGateway;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressGateway addressGateway;

    public AddressService (AddressGateway addressGateway) {
        this.addressGateway = addressGateway;
    }

    public AddressResponse findAddressById (Long addressId) {
        return addressGateway.getAddress (addressId);
    }
}
