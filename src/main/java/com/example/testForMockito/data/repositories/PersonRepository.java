package com.example.testForMockito.data.repositories;

import com.example.testForMockito.data.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
}
