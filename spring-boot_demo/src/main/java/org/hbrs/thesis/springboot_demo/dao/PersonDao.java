package org.hbrs.thesis.springboot_demo.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hbrs.thesis.springboot_demo.config.ApplicationConfig;
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
  private ApplicationConfig applicationConfig;
  @PersistenceContext
  private EntityManager entityManager;

  public PersonDao(PersonRepository personRepository, ApplicationConfig applicationConfig) {
    this.personRepository = personRepository;
    this.applicationConfig = applicationConfig;
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

  @Transactional
  public void insertPersonToDb(Person person) {
    entityManager
        .createNativeQuery("INSERT INTO " + applicationConfig.getPostgresTable()
            + " (firstName, lastName, birthDate, timestamp) VALUES (?, ?, ?, ?)")
        .setParameter(1, person.getFirstName()).setParameter(2, person.getLastName())
        .setParameter(3, person.getBirthDate()).setParameter(4, new Timestamp(Instant.now().toEpochMilli())).executeUpdate();
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

  public void updatePersonById(Person person) {
    Optional<Person> optionalPerson = getPersonById(person.getId());
    if (optionalPerson.isEmpty()) {
      throw new EntityNotFoundException("The person with the id: " + person.getId() + " does not exist!");
    }
    person.setTimestamp(optionalPerson.get().getTimestamp());
    personRepository.save(person);
  }
}
