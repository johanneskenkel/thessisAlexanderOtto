package org.hbrs.thesis.springboot_demo.service;

import org.hbrs.thesis.springboot_demo.dto.MessageDto;
import org.hbrs.thesis.springboot_demo.dao.PersonDao;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
  private PersonDao personDao;

  public PersonService(PersonDao personDao) {
    this.personDao = personDao;
  }

  public MessageDto generatePersonsToDB(long numberOfPersonsToGenerate) {
    personDao.insertNumberOfRandomPersonsToDB(numberOfPersonsToGenerate);
    return new MessageDto("You have successfully generated " + numberOfPersonsToGenerate + " persons in the DB");
  }
}
