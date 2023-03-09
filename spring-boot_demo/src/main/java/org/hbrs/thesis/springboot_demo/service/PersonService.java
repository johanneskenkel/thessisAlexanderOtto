package org.hbrs.thesis.springboot_demo.service;

import org.hbrs.thesis.springboot_demo.dto.MessageDto;
import org.hbrs.thesis.springboot_demo.model.Person;

import java.util.List;

import org.hbrs.thesis.springboot_demo.config.ApplicationConfig;
import org.hbrs.thesis.springboot_demo.dao.PersonDao;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
  private PersonDao personDao;
  private ApplicationConfig applicationConfig;

  public PersonService(PersonDao personDao, ApplicationConfig applicationConfig) {
    this.personDao = personDao;
    this.applicationConfig = applicationConfig;
  }

  public MessageDto generatePersonsToDB(long numberOfPersonsToGenerate) {
    personDao.insertNumberOfRandomPersonsToDB(numberOfPersonsToGenerate);
    return new MessageDto("You have successfully generated " + numberOfPersonsToGenerate + " persons in the DB");
  }

  public List<Person> getAllPersons() {
    return personDao.getAllPersons();
  }

  public MessageDto removeDBTable() {
    this.personDao.dropDBTable();
    return new MessageDto("You have successfully deleted the " + applicationConfig.getPostgresTable() + " table");
  }
}
