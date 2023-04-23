package org.hbrs.thesis.springboot_demo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hbrs.thesis.springboot_demo.SpringBootDemoApplication;
import org.hbrs.thesis.springboot_demo.config.ApplicationConfig;
import org.hbrs.thesis.springboot_demo.model.Person;
import org.hbrs.thesis.springboot_demo.repository.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.util.List;

// Currently the tests use the same persons Database Table name
@ActiveProfiles("test")
@SpringBootTest(classes = SpringBootDemoApplication.class)
class PersonDaoTest {
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  ApplicationConfig applicationConfig;

  private PersonDao personDao;

  @BeforeEach
  void setup() {
    personDao = new PersonDao(personRepository, applicationConfig);
  }

  @AfterEach
  void teardown() {
          personDao.dropDBTable();
  }

  @Test
  void assureThatThousandPersonsAreGenerated() {
    long numberOfPersonsToGenerate = 1000;
    personDao.generateNumberOfRandomPersonsToDB(1000);
    assertEquals(numberOfPersonsToGenerate, personDao.getAllPersons().size());
  }

  @Test
  void assureThatPersonsGeneratedCorrectly() {
      long numberOfPersonsToGenerate = 100;
      personDao.generateNumberOfRandomPersonsToDB(numberOfPersonsToGenerate);
      List<Person> persons = personDao.getAllPersons();
      for (Person person : persons) {
          assertFalse(person.getFirstName().isBlank());
          assertFalse(person.getLastName().isBlank());
          assertFalse(person.getBirthDate().toString().isBlank());
          assertFalse(person.getTimestamp().toString().isBlank());
          long id = person.getId();
          assertFalse(id < 0 && id > numberOfPersonsToGenerate);
      }
  }

  @Test
  void assureThatPersonInsertionWorksCorrectly() {
      personDao.generateNumberOfRandomPersonsToDB(100);
      Person person = new Person(0, "John", "Test", new Date(1182054510000L), null);
      personDao.insertPersonToDb(person);
      Person personFromDb = personDao.getPersonById(101L).get();
      assertEquals(person.getFirstName(), personFromDb.getFirstName());
      assertEquals(person.getLastName(), personFromDb.getLastName());
      assertEquals("2007-06-17", personFromDb.getBirthDate().toString());
      assertNotNull(personFromDb.getTimestamp());
  }

  @Test
  void assureThatPersonUpdateWorksCorrectly(){
      personDao.generateNumberOfRandomPersonsToDB(10);
      Person person = new Person(7, "John", "Test", new Date(1182054510000L), null);
      personDao.updatePerson(person);
      Person personFromDb = personDao.getPersonById(7L).get();
      assertEquals(person.getFirstName(), personFromDb.getFirstName());
      assertEquals(person.getId(), personFromDb.getId());
      assertEquals(person.getLastName(), personFromDb.getLastName());
      assertEquals("2007-06-17", personFromDb.getBirthDate().toString());
  }

  @Test
  void assureThatPersonIsDeletedCorrectlyById() {
      personDao.generateNumberOfRandomPersonsToDB(20);
      personDao.deletePersonById(20L);
      personDao.deletePersonById(10L);
      assertTrue(personDao.getPersonById(20L).isEmpty());
      assertTrue(personDao.getPersonById(10L).isEmpty());
  }
}
