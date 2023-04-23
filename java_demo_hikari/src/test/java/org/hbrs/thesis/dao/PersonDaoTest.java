package org.hbrs.thesis.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.hbrs.thesis.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class PersonDaoTest {
    private PersonDao personDao = new PersonDao();

    @AfterEach
    void teardown() {
        try {
            personDao.dropDBTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void assureThatThousandPersonsAreGenerated() throws SQLException {
        long numberOfPersonsToGenerate = 1000;
        personDao.generateNumberOfRandomPersonsToDB(1000);
        assertEquals(numberOfPersonsToGenerate, personDao.getAllPersons().size());
    }

    @Test
    void assureThatPersonsGeneratedCorrectly() throws SQLException {
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
    void assureThatPersonInsertionWorksCorrectly() throws SQLException {
        personDao.generateNumberOfRandomPersonsToDB(100);
        Person person = new Person(0, "John", "Test", new Date(1182054510000L), null);
        personDao.insertPerson(person);
        Person personFromDb = personDao.getPersonById(101);
        assertEquals(person.getFirstName(), personFromDb.getFirstName());
        assertEquals(person.getLastName(), personFromDb.getLastName());
        assertEquals("2007-06-17", personFromDb.getBirthDate().toString());
        assertNotNull(personFromDb.getTimestamp());
    }

    @Test
    void assureThatPersonUpdateWorksCorrectly() throws SQLException {
        personDao.generateNumberOfRandomPersonsToDB(10);
        Person person = new Person(7, "John", "Test", new Date(1182054510000L), null);
        personDao.updatePerson(person);
        Person personFromDb = personDao.getPersonById(7);
        assertEquals(person.getFirstName(), personFromDb.getFirstName());
        assertEquals(person.getId(), personFromDb.getId());
        assertEquals(person.getLastName(), personFromDb.getLastName());
        assertEquals("2007-06-17", personFromDb.getBirthDate().toString());
    }

    @Test
    void assureThatPersonIsDeletedCorrectlyById() throws SQLException {
        personDao.generateNumberOfRandomPersonsToDB(20);
        personDao.deletePersonById(20);
        personDao.deletePersonById(10);
        assertNull(personDao.getPersonById(20));
        assertNull(personDao.getPersonById(10));
    }
}
