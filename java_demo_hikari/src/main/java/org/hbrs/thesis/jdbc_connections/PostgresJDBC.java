package org.hbrs.thesis.jdbc_connections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hbrs.thesis.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariDataSource;

public class PostgresJDBC {
    private static Logger logger = LoggerFactory.getLogger(PostgresJDBC.class.getName());
    private ApplicationConfig applicationConfig = new ApplicationConfig();
    private HikariDataSource hikariDataSource;

    public PostgresJDBC() {
        hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(applicationConfig.getPostgresUrl());
        hikariDataSource.setUsername(applicationConfig.getPostgresUsername());
        hikariDataSource.setPassword(applicationConfig.getPostgresPassword());
        hikariDataSource.addDataSourceProperty("cachePrepStmts", "true");
        hikariDataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariDataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        try {
            createPersonsTable();
        } catch (SQLException ex) {
            logger.warn("Table creation failed with the message: {}", ex.getMessage());
        }
    }

    public Connection createPostgresConnection() {
        Connection connection = null;
        try {
            connection = hikariDataSource.getConnection();
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
