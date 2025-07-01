package org.me.practise.addressservice.service;

import lombok.extern.slf4j.Slf4j;
import org.me.practise.addressservice.entity.Address;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddressService {

    public Address getAllAddresses() {
        log.info("Fetching all addresses");
        return new Address("654 Pine St", "North Haverbrook", "IL", "62902");
    }
}
