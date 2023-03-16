package org.hbrs.thesis.springboot_demo.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hbrs.thesis.springboot_demo.model.Person;
import org.hbrs.thesis.springboot_demo.repository.PersonRepository;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class PersonDao {
  private Faker faker = new Faker();
  private PersonRepository personRepository;

  @PersistenceContext
  private EntityManager entityManager;

  public PersonDao(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public List<Person> getAllPersons() {
    return this.personRepository.findAll();
  }

  public Optional<Person> getPersonById(Long id) {
    return personRepository.findById(id);
  }

  public List<Person> getNumberOfPersons(int numberOfPersons) {
    return personRepository.findNumberOfPersons(numberOfPersons);
  }

  public void insertNumberOfRandomPersonsToDB(long numberOfPersonsToGenerate) {
    List<Person> personList = new ArrayList<>();
    for (int i = 0; i < numberOfPersonsToGenerate; ++i) {
      personList.add(generatePerson());
    }
    personRepository.saveAll(personList);
  }

  private Person generatePerson() {
    String firstName = faker.name().firstName();
    String lastName = faker.name().lastName();
    java.util.Date birthUtilDate = faker.date().birthday();
    Date birthDate = new Date(birthUtilDate.getTime());
    Timestamp timestamp = new Timestamp(Instant.now().toEpochMilli());
    return new Person(0, firstName, lastName, birthDate, timestamp);
  }

  @Transactional
  public void dropDBTable() {
    entityManager.createNativeQuery("DROP TABLE persons").executeUpdate();
  }

  public void removePersonById(Long id) {
    personRepository.deleteById(id);
  }

  public void updatePersonById(Long id, Person person) {
    if (getPersonById(id).isEmpty() ) {
      throw new EntityNotFoundException("The person with the id: " + " does not exist!");
    }
    personRepository.save(person);
  }
}
