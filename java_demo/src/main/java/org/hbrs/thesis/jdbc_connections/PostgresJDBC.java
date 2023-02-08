package org.hbrs.thesis.jdbc_connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class PostgresJDBC {
    private static Logger logger = Logger.getLogger(PostgresJDBC.class.getName());

    public PostgresJDBC() {
        try {
            createPersonsTable();
        } catch (SQLException ex) {
            logger.warning("Table creation failed with the message: " + ex.getMessage());
        }
    }

    public Connection createPostgresConnection() {
        String url = "jdbc:postgresql://localhost/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "12345");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, props);
        } catch (SQLException ex) {
            logger.warning("Connection to postgres DB failed with the message: " + ex.getMessage());
        }

        return connection;
    }

    private void createPersonsTable() throws SQLException {
        try (Connection connection = createPostgresConnection()) {
            if (connection != null) {
                PreparedStatement prepareStatement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS persons (id SERIAL PRIMARY KEY, firstName VARCHAR(30), lastName VARCHAR(30), age INTEGER, date TIMESTAMP)");

                prepareStatement.executeQuery();
            } else {
                logger.warning("Couldn't create table, because the connection was null");
            }
        }
    }
}
