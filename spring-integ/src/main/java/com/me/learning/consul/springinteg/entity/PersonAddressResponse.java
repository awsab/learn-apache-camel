package com.me.learning.consul.springinteg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonAddressResponse {

    private PersonResponse personResponse;
    private AddressResponse addressResponse;
}
