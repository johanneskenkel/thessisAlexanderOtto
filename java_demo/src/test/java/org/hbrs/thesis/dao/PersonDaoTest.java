package org.hbrs.thesis.dao;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class PersonDaoTest {
    @Test
    void assureThatInsertToPersonsWorks() throws SQLException {
        // PersonDao.insertToDB();
        // PersonDao.getAllPersonsFromDb();
        PersonDao.dropPersonsTable();
    }
}
