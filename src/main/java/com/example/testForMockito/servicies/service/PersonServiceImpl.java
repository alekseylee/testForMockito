package com.example.testForMockito.servicies.service;

import com.example.testForMockito.data.entities.PersonEntity;
import com.example.testForMockito.data.repositories.PersonRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    private PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonEntity savePerson(PersonEntity person) {

        Optional<PersonEntity> savedPerson = personRepository.findById(person.getId());
        if(savedPerson.isPresent()){
            throw new RuntimeException("Person already exist with given id:" + person.getId());
        }
        return personRepository.save(person);
    }


    @Override
    public List<PersonEntity> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public Optional<PersonEntity> getPersonById(long id) {
        return personRepository.findById(id);
    }

    @Override
    public PersonEntity updatePerson(PersonEntity updatePerson) {
        return personRepository.save(updatePerson);
    }

    @Override
    public void deletePerson(long id) {
        personRepository.deleteById(id);
    }
}
