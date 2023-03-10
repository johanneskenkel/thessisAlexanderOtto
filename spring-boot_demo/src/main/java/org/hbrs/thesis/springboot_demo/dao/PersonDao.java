package org.hbrs.thesis.springboot_demo.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.hbrs.thesis.springboot_demo.model.Person;
import org.hbrs.thesis.springboot_demo.repository.PersonRepository;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;

@Service
public class PersonDao {
  private static Random random = new Random();
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
    int age = 18 + random.nextInt(83);
    Timestamp timestamp = new Timestamp(new Date().getTime());
    return new Person(0, firstName, lastName, age, timestamp);
  }

  @Transactional
  public void dropDBTable() {
    entityManager.createNativeQuery("DROP TABLE persons").executeUpdate();
  }

  public void removePersonById(Long id) {
    personRepository.deleteById(id);
  }
}
