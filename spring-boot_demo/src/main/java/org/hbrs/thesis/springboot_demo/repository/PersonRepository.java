package org.hbrs.thesis.springboot_demo.repository;

import org.hbrs.thesis.springboot_demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    
    @Query(
        value = "SELECT * FROM persons LIMIT ?1", 
        nativeQuery = true)
      List<Person> findNumberOfPersons(int numberOfPersons);
}
