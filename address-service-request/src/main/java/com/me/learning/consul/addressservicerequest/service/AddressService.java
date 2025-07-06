/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.addressservicerequest.service;

import com.me.learning.consul.addressservicerequest.entity.AddressResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AddressService {

    private static final List<AddressResponse> addressList = new ArrayList<> ();

    static {
        addressList.add (AddressResponse.builder ()
                .addressId (1L)
                .addressLine1 ("123 Main St")
                .addressLine2 ("Apt 4B")
                .city ("Springfield")
                .state ("IL")
                .country ("USA")
                .postalCode ("62701")
                .build ());

        addressList.add (AddressResponse.builder ()
                .addressId (2L)
                .addressLine1 ("456 Elm St")
                .addressLine2 ("Suite 200")
                .city ("Shelbyville")
                .state ("IL")
                .country ("USA")
                .postalCode ("62565")
                .build ());

        addressList.add (AddressResponse.builder ()
                .addressId (3L)
                .addressLine1 ("789 Oak St")
                .addressLine2 ("Unit 5")
                .city ("Capital City")
                .state ("IL")
                .country ("USA")
                .postalCode ("62702")
                .build ());

        addressList.add (AddressResponse.builder ()
                .addressId (4L)
                .addressLine1 ("321 Maple St")
                .addressLine2 ("Floor 3")
                .city ("Ogdenville")
                .state ("IL")
                .country ("USA")
                .postalCode ("62550")
                .build ());

        addressList.add (AddressResponse.builder ()
                .addressId (5L)
                .addressLine1 ("654 Pine St")
                .addressLine2 ("Building A")
                .city ("North Haverbrook")
                .state ("IL")
                .country ("USA")
                .postalCode ("62555")
                .build ());
    }

    public AddressResponse getAddressById(Long addressId) {
        log.info("Fetching address with id: {}", addressId);
        return addressList.stream()
                .filter(address -> address.getAddressId().equals(addressId))
                .findFirst()
                .orElse(null);
    }
}
