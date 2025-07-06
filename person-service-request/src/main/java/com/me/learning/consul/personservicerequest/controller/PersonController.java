/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.personservicerequest.controller;

import com.me.learning.consul.personservicerequest.entiry.PersonResponse;
import com.me.learning.consul.personservicerequest.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/person")
@Slf4j
public class PersonController {

    private final PersonService personService;

    public PersonController (PersonService personService) {
        this.personService = personService;
    }

    @GetMapping ("/{personId}")
    public PersonResponse getPersonById(@PathVariable Long personId) {
        log.info("Received request to get person by id: {}", personId);
        return personService.getPersonById(personId);
    }
}
