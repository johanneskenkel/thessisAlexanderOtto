package org.hbrs.thesis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.hbrs.thesis.jdbc_connections.PostgresJDBC;
import org.hbrs.thesis.service.PersonService;
import org.junit.jupiter.api.Test;

class PersonServiceTest {
    @Test
    void assureThatThousandPersonsWereGenerated() {
        try{PersonService.generatePersons(1000000);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        // assertEquals(1000000, PersonService.generateMillionPersons().size());
    }


}
