package org.hbrs.thesis.jdbc_connection;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.hbrs.thesis.jdbc_connections.PostgresJDBC;
import org.junit.jupiter.api.Test;

class PostgresJDBCTest {
    @Test
    void assureThatPostgresJDBCConnectionCouldBeCreated() throws SQLException {
        try (Connection connection = new PostgresJDBC().createPostgresConnection()) {
            assertEquals(true, connection.isValid(10));
        }
    }
}
