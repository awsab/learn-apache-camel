package com.example.person.controller;

import com.example.person.entity.AggregationRequest;
import com.example.person.entity.Person;
import com.example.person.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/all")
    public Person getAllPersons() {
        return personService.getAllPersons ();
    }

    @PostMapping()
    public ResponseEntity<Person> addPerson(@RequestBody AggregationRequest request) {
        System.out.println("Received request to add person: " + request);
        return ResponseEntity.ok ( new Person ( 5L, "Charlie", "Davis", "Charlie@Davis.com", "333-333-3333"));
    }
}
