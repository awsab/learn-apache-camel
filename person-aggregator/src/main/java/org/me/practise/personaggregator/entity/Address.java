package org.me.practise.personaggregator.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private String address;
    private String city;
    private String state;
    private String zipCode;

    public Address() {
    }

}
