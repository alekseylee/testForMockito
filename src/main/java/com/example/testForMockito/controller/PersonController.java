package com.example.testForMockito.controller;

import com.example.testForMockito.data.entities.PersonEntity;
import com.example.testForMockito.servicies.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonEntity createPerson(@RequestBody PersonEntity person){
        return personService.savePerson(person);
    }

    @GetMapping
    public List<PersonEntity> getAllPersons(){
        return personService.getAllPersons();
    }

    @GetMapping("{id}")
    public ResponseEntity<PersonEntity> getPersonById(@PathVariable("id") Long id){
        return personService.getPersonById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<PersonEntity> updateEmployee(@PathVariable("id") Long id,
                                                   @RequestBody PersonEntity person){
        return personService.getPersonById(id)
                .map(savedPerson -> {

                    savedPerson.setFirstName(person.getFirstName());
                    savedPerson.setLastName(person.getLastName());
                    savedPerson.setEmail(person.getEmail());

                    PersonEntity updatePerson = personService.updatePerson(savedPerson);
                    return new ResponseEntity<>(updatePerson, HttpStatus.OK);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long id){

        personService.deletePerson(id);

        return new ResponseEntity<String>("Person deleted successfully!.", HttpStatus.OK);

    }
}
