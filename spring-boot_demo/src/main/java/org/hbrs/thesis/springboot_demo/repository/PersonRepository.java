package org.hbrs.thesis.springboot_demo.repository;

import org.hbrs.thesis.springboot_demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
  
}
