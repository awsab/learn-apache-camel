package org.me.practise.personaggregator.entity;

import lombok.Data;


@Data
public class PersonAggregatedResponse {
    private Person person;
    private Address address;
    private SoapAccount soapAccount;

    private String personServiceError;
    private String addressServiceError;
    private String soapServiceError;
}
