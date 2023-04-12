package org.hbrs.thesis.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import org.hbrs.thesis.config.ApplicationConfig;
import org.hbrs.thesis.dao.PersonDao;
import org.hbrs.thesis.dto.MessageDto;
import org.hbrs.thesis.model.Person;

public class PersonService {
    private PersonDao personDao;
    private ApplicationConfig applicationConfig;

    public PersonService() {
        personDao = new PersonDao();
        this.applicationConfig = new ApplicationConfig();
    }

    public List<Person> getAllPersons() throws SQLException {
        return personDao.getAllPersons();
    }

    public Person getPersonById(long id) throws SQLException {
        return personDao.getPersonById(id);
    }

    public List<Person> getNumberOfPersons(int numberOfPersons) throws SQLException{
        return this.personDao.getNumberOfPersons(numberOfPersons);
    }

    public MessageDto insertPersonToDb(Person person) throws SQLException {
        personDao.insertPerson(person);
        return new MessageDto("You habe successfully inserted a person");
    }

    public MessageDto generateRandomPersonsToDB(long numberOfPersonsToGenerate) throws SQLException {
        personDao.generateNumberOfRandomPersonsToDB(numberOfPersonsToGenerate);
        return new MessageDto("You have successfully generated " + numberOfPersonsToGenerate + " persons in the DB");
    }

    public MessageDto removeDBTable() throws SQLException {
        personDao.dropDBTable();
        return new MessageDto("You have successfully deleted the " + applicationConfig.getPostgresTable() + " table");
    }

    public MessageDto updatePerson(Person person) throws SQLException {
        personDao.updatePerson(person);
        return new MessageDto("You habe successfully updated the person with the id: " + person.getId());
    }
}
