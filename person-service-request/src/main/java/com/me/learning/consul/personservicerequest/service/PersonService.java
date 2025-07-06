/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.personservicerequest.service;

import com.me.learning.consul.personservicerequest.entiry.PersonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PersonService {

    private static List<PersonResponse> personList = new ArrayList<> ();

    static {
        personList.add(new PersonResponse(1L, "John", "Doe"));
        personList.add(new PersonResponse(2L, "Jane", "Smith"));
        personList.add(new PersonResponse(3L, "Alice", "Johnson"));
        personList.add(new PersonResponse(4L, "Bob", "Brown"));
        personList.add(new PersonResponse(5L, "Charlie", "Davis"));
        personList.add(new PersonResponse(6L, "Eve", "Wilson"));
        personList.add(new PersonResponse(7L, "Frank", "Garcia"));
        personList.add(new PersonResponse(8L, "Grace", "Martinez"));
        personList.add(new PersonResponse(9L, "Hank", "Lopez"));
        personList.add(new PersonResponse(10L, "Ivy", "Gonzalez"));
    }


    public PersonResponse getPersonById(Long personId) {
        log.info("Fetching person by id: {}", personId);
        return personList.stream()
                .filter(person -> person.getPersonId().equals(personId))
                .findFirst()
                .orElse(null);
    }
}
