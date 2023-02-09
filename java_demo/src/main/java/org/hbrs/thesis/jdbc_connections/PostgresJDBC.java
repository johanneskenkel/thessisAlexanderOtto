package org.hbrs.thesis.jdbc_connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import org.hbrs.thesis.config.ApplicationConfig;

public class PostgresJDBC {
    private static Logger logger = Logger.getLogger(PostgresJDBC.class.getName());
    private ApplicationConfig applicationConfig;

    public PostgresJDBC() {
        this.applicationConfig = new ApplicationConfig();
        try {
            createPersonsTable();
        } catch (SQLException ex) {
            logger.warning("Table creation failed with the message: " + ex.getMessage());
        }
    }

    public Connection createPostgresConnection() {
        String url = applicationConfig.getPostgresUrl();
        Properties dbProperties = new Properties();
        dbProperties.setProperty("user", applicationConfig.getPostgresUsername());
        dbProperties.setProperty("password", applicationConfig.getPostgresPassword());
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, dbProperties);
        } catch (SQLException ex) {
            logger.warning("Connection to postgres DB failed with the message: " + ex.getMessage());
        }

        return connection;
    }

    protected void createPersonsTable() throws SQLException {
        try (Connection connection = createPostgresConnection()) {
            if (connection != null) {
                try (PreparedStatement prepareStatement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS " + applicationConfig.getPostgresTable() + " (id SERIAL PRIMARY KEY, firstName VARCHAR(30), lastName VARCHAR(30), age INTEGER, date TIMESTAMP)")) {

                    prepareStatement.executeUpdate();
                }
            } else {
                logger.warning("Couldn't create the " + applicationConfig.getPostgresTable() + " table, because the connection was null");
            }
        }
    }
}
