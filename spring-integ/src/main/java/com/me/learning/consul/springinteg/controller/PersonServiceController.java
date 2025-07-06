/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.springinteg.controller;

import com.me.learning.consul.springinteg.entity.PersonResponse;
import com.me.learning.consul.springinteg.service.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/person-controller")
public class PersonServiceController {

    private final PersonService personService;

    public PersonServiceController (PersonService personService) {
        this.personService = personService;
    }

    @GetMapping ("/{personId}")
    public PersonResponse getPersonById (@PathVariable Long personId) {
        return personService.findByPersonId (personId);
    }
}
