package org.hbrs.thesis.service;

import java.sql.SQLException;
import java.util.List;

import org.hbrs.thesis.config.ApplicationConfig;
import org.hbrs.thesis.dao.PersonDao;
import org.hbrs.thesis.dto.MessageDto;
import org.hbrs.thesis.model.Person;

public class PersonService {
    PersonDao personDao;
    ApplicationConfig applicationConfig;

    public PersonService() {
        personDao = new PersonDao();
        this.applicationConfig = new ApplicationConfig();
    }

    public List<Person> getAllPersons() throws SQLException {
        return personDao.getAllFromDb();
    }

    public MessageDto generatePersonsToDB(long numberOfPersonsToGenerate) throws SQLException {
        personDao.insertNumberOfRandomPersonsToDB(numberOfPersonsToGenerate);
        return new MessageDto("You have successfully generated " + numberOfPersonsToGenerate + " persons in the DB");
    }

    public MessageDto removeDBTable() throws SQLException {
        personDao.dropDBTable();
        return new MessageDto("You have successfully deleted the " + applicationConfig.getPostgresTable() + " table");
    }
}
