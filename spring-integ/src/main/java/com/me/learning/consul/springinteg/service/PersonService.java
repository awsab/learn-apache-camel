/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.springinteg.service;

import com.me.learning.consul.springinteg.entity.PersonRequest;
import com.me.learning.consul.springinteg.entity.PersonResponse;
import com.me.learning.consul.springinteg.gateway.PersonGateway;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonGateway personGateway;

    public PersonService (PersonGateway personGateway) {
        this.personGateway = personGateway;
    }

    public PersonResponse findByPersonId (Long personId) {
        return personGateway.getPerson(personId);
    }
}
