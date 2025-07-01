package org.me.practise.personaggregator.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person {
    private Long personId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public Person() {
    }
}
