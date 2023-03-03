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

    public MessageDto randomCalculation() {
        double res = 0;
        Random random = new Random();
        for(int i=0; i<1e9; ++i) {
             res = random.nextDouble()+0.1 / random.nextDouble() + 0.1;
        }
        return new MessageDto("Random Success " + res);
    }
}
