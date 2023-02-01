package org.hbrs.thesis.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hbrs.thesis.jdbc_connections.PostgresJDBC;
import org.junit.jupiter.api.Test;

class PersonServiceTest {
    @Test
    void assureThatTenThousandPersonsWereGenerated() {
        assertEquals(1000000, PersonService.generateMillionPersons().size());
    }


}
