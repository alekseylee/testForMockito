package com.example.testForMockito;

import com.example.testForMockito.data.entities.PersonEntity;
import com.example.testForMockito.servicies.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest
public class PersonControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PersonService personService;
    @Autowired
    private ObjectMapper objectMapper;


    // given - precondition or setup
    @Test
    public void givenPersonObject() throws Exception {
        PersonEntity person = PersonEntity.builder()
                .firstName("Sheldon")
                .lastName("Cooper")
                .email("thebigbang@gmail.com")
                .build();
        given(personService.savePerson(any(PersonEntity.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/v1/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(person.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(person.getEmail())));
    }

    // JUnit test for Get All employees REST API
    @Test
    public void givenListOfPersons_whenGetAllPersons_thenReturnPersonsList() throws Exception{
        // given - precondition or setup
        List<PersonEntity> listOfPersons = new ArrayList<>();
        listOfPersons.add(PersonEntity.builder().firstName("Benedict").lastName("Cumberbatch").email("benny@gmail.com").build());
        listOfPersons.add(PersonEntity.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
        given(personService.getAllPersons()).willReturn(listOfPersons);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/persons"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfPersons.size())));

    }

    // positive scenario - valid employee id
    // JUnit test for GET employee by id REST API
    @Test
    public void givenPersonId_whenGetPersonById_thenReturnEmployeeObject() throws Exception{
        // given - precondition or setup
        Long id = 1L;
        PersonEntity person = PersonEntity.builder()
                .firstName("Ricky")
                .lastName("Martin")
                .email("rickytikitaki@gmail.com")
                .build();
        given(personService.getPersonById(id)).willReturn(Optional.of(person));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/persons/{id}", id));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));

    }

    // negative scenario - valid employee id
    // JUnit test for GET employee by id REST API
    @Test
    public void givenInvalidPersonId_whenGetPersonById_thenReturnEmpty() throws Exception{
        // given - precondition or setup
        Long id = 1L;
        PersonEntity person = PersonEntity.builder()
                .firstName("Ben")
                .lastName("Klark")
                .email("superman@gmail.com")
                .build();
        given(personService.getPersonById(id)).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}", id));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }
    // JUnit test for update employee REST API - positive scenario
    @Test
    public void givenUpdatedPerson_whenUpdatePerson_thenReturnUpdatePersonObject() throws Exception{
        // given - precondition or setup
        Long id = 1L;
        PersonEntity savedPerson = PersonEntity.builder()
                .firstName("Mike")
                .lastName("Tyson")
                .email("biteyourear@gmail.com")
                .build();

        PersonEntity updatePerson = PersonEntity.builder()
                .firstName("Mike")
                .lastName("Tyson")
                .email("bye@gmail.com")
                .build();
        given(personService.getPersonById(id)).willReturn(Optional.of(savedPerson));
        given(personService.updatePerson(any(PersonEntity.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/v1/persons/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePerson)));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatePerson.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatePerson.getLastName())))
                .andExpect(jsonPath("$.email", is(updatePerson.getEmail())));
    }

    // JUnit test for update employee REST API - negative scenario
    @Test
    public void givenUpdatedPerson_whenUpdatePerson_thenReturn404() throws Exception{
        // given - precondition or setup
        Long id = 1L;
        PersonEntity savedPerson = PersonEntity.builder()
                .firstName("Pablo")
                .lastName("Escobar")
                .email("pablito@gmail.com")
                .build();

        PersonEntity updatedPerson = PersonEntity.builder()
                .firstName("Pab")
                .lastName("esco")
                .email("pab@gmail.com")
                .build();
        given(personService.getPersonById(id)).willReturn(Optional.empty());
        given(personService.updatePerson(any(PersonEntity.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/v1/persons/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for delete employee REST API
    @Test
    public void givenPersonId_whenDeletePerson_thenReturn200() throws Exception{
        // given - precondition or setup
        Long id = 1L;
        willDoNothing().given(personService).deletePerson(id);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/v1/persons/{id}", id));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}


