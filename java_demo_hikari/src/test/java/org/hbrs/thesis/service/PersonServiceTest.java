package org.hbrs.thesis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.hbrs.thesis.dto.MessageDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

class PersonServiceTest {
    private PersonService personService = new PersonService();

    @AfterEach
    void teardown(TestInfo testInfo) {
        if(testInfo.getTags().contains("SkipCleanup")) {
            return;
        }
        try {
            personService.removeDBTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void assureThatPersonGenerationReturnsCorrectMessage() throws SQLException {
        long numberOfPersonsToGenerate = 1000;
        MessageDto messageDto = personService.generateRandomPersonsToDB(numberOfPersonsToGenerate);
        assertEquals("You have successfully generated " + numberOfPersonsToGenerate + " persons in the DB",
                messageDto.getMessage());
    }

    @Test
    @Tag("SkipCleanup")
    void assureThatTableRemovalReturnsCorrectMessage() throws SQLException {
        personService.generateRandomPersonsToDB(1);
        MessageDto messageDto = personService.removeDBTable();
        assertEquals("You have successfully deleted the test table", messageDto.getMessage());
    }
}
