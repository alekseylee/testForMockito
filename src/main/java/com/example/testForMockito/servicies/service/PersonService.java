package com.example.testForMockito.servicies.service;

import com.example.testForMockito.data.entities.PersonEntity;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    PersonEntity savePerson(PersonEntity Person);
    List<PersonEntity> getAllPersons();
    Optional<PersonEntity> getPersonById(long id);
    PersonEntity updatePerson(PersonEntity updatedPerson);
    void deletePerson(long id);
}
