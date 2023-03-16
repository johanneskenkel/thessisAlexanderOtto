package org.hbrs.thesis.springboot_demo.service;

import java.util.List;
import java.util.Optional;

import org.hbrs.thesis.springboot_demo.config.ApplicationConfig;
import org.hbrs.thesis.springboot_demo.dao.PersonDao;
import org.hbrs.thesis.springboot_demo.dto.MessageDto;
import org.hbrs.thesis.springboot_demo.model.Person;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PersonService {
  private PersonDao personDao;
  private ApplicationConfig applicationConfig;

  public PersonService(PersonDao personDao, ApplicationConfig applicationConfig) {
    this.personDao = personDao;
    this.applicationConfig = applicationConfig;
  }

  public List<Person> getAllPersons() {
    return personDao.getAllPersons();
  }

  public Person getPersonById(long id) {
    Optional<Person> personOptional = this.personDao.getPersonById(id);
    if (personOptional.isEmpty()) {
      throw new EntityNotFoundException("The person with the id: " + " does not exist!");
    }
    return personOptional.get();
  }

  public List<Person> getNumberOfPersons(int numberOfPersons) {
    return this.personDao.getNumberOfPersons(numberOfPersons);
  }

  public MessageDto generatePersonsToDB(Long numberOfPersonsToGenerate) {
    personDao.insertNumberOfRandomPersonsToDB(numberOfPersonsToGenerate);
    return new MessageDto("You have successfully generated " + numberOfPersonsToGenerate + " persons in the DB");
  }

  public MessageDto removeDBTable() {
    this.personDao.dropDBTable();
    return new MessageDto("You have successfully deleted the " + applicationConfig.getPostgresTable() + " table");
  }

  public MessageDto removePersonById(Long id) {
    try {
      this.personDao.removePersonById(id);
    } catch (EmptyResultDataAccessException ex) {
      throw new EntityNotFoundException("The person with the id: " + " does not exist!");
    }
    return new MessageDto("You habe successfully deleted the person with the id: " + id);
  }

  public MessageDto updatePersonById(Long id, Person person) {
    this.personDao.updatePersonById(id, person);
    return new MessageDto("You habe successfully deleted the person with the id: " + id);
  }
}
