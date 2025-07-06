/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.springinteg.gatewayold;

import com.me.learning.consul.springinteg.entity.AddressResponse;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway (
        name = "addressGateway",
        defaultRequestChannel = "addressRequestChannel")
public interface AddressGateway {
    AddressResponse getAddress (Long addressId);
}
