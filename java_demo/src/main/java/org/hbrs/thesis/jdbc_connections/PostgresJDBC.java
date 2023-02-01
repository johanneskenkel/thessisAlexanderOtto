package org.hbrs.thesis.jdbc_connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresJDBC {

    public Connection createPostgresJDBCConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "12345");
        Connection conn = null;
        conn = DriverManager.getConnection(url, props);
        return conn;
    }
}
