package org.hbrs.thesis.service;

import java.sql.SQLException;
import java.util.List;

import org.hbrs.thesis.dao.PersonDao;
import org.hbrs.thesis.dto.MessageDto;
import org.hbrs.thesis.model.Person;

public class PersonService {
    public static List<Person> getAllPersons() throws SQLException {
        return PersonDao.getAllPersonsFromDb();
    }

    public static MessageDto generatePersons(long numberOfPersonsToGenerate) throws SQLException {
        PersonDao.insertPersonsToDB(numberOfPersonsToGenerate);
        return new MessageDto("You have successfully generated " + numberOfPersonsToGenerate + " persons in the DB");
    }

    public static MessageDto removePersonTable() throws SQLException {
        PersonDao.dropPersonsTable();
        return new MessageDto("You have successfully deleted the persons table");
    }
}
