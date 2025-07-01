package com.example.person.service;

import com.example.person.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PersonService {

    public Person getAllPersons() {
        log.info("Fetching all persons");

        return new Person ( 5L, "Charlie", "Davis", "Charlie@Davis.com", "333-333-3333");

    }
}
