package org.hbrs.thesis.jdbc_connection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.sql.SQLException;

import org.hbrs.thesis.jdbc_connections.PostgresJDBC;
import org.junit.jupiter.api.Test;

class PostgresJDBCTest {
    @Test
    void assureThatPostgresJDBCConnectionCouldBeCreated() throws SQLException {
        PostgresJDBC postgresJDBC = new PostgresJDBC();
        assertDoesNotThrow(() -> postgresJDBC.createPostgresJDBCConnection());
    }
}
