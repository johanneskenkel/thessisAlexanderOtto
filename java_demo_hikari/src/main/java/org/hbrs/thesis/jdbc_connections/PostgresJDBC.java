package org.hbrs.thesis.jdbc_connections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.hbrs.thesis.config.ApplicationConfig;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgresJDBC {
    private static Logger logger = LoggerFactory.getLogger(PostgresJDBC.class.getName());
    private ApplicationConfig applicationConfig;
    private static HikariDataSource hikariDataSource;

    public PostgresJDBC() {
        this.applicationConfig = new ApplicationConfig();
        String url = applicationConfig.getPostgresUrl();
        Properties dbProperties = new Properties();
        dbProperties.setProperty("user", applicationConfig.getPostgresUsername());
        dbProperties.setProperty("password", applicationConfig.getPostgresPassword());
        pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL(url);
        pgSimpleDataSource.setUser(applicationConfig.getPostgresUsername());
        pgSimpleDataSource.setPassword(applicationConfig.getPostgresPassword());
        try {
            createPersonsTable();
        } catch (SQLException ex) {
            logger.warn("Table creation failed with the message: {}", ex.getMessage());
        }
    }

    public Connection createPostgresConnection() {
        Connection connection = null;
        try {
            connection = pgSimpleDataSource.getConnection();
        } catch (SQLException ex) {
            logger.warn("Connection to postgres DB failed with the message: {}", ex.getMessage());
        }

        return connection;
    }

    protected void createPersonsTable() throws SQLException {
        try (Connection connection = createPostgresConnection()) {
            if (connection != null) {
                try (PreparedStatement prepareStatement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS " + applicationConfig.getPostgresTable()
                                + " (id SERIAL PRIMARY KEY, firstName VARCHAR(30), lastName VARCHAR(30), birthDate DATE, timestamp TIMESTAMP)")) {
                    prepareStatement.executeUpdate();
                }
            } else {
                logger.warn("Couldn't create the {} table, because the connection was null",
                        applicationConfig.getPostgresTable());
            }
        }
    }
}
