package org.hbrs.thesis.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hbrs.thesis.config.ApplicationConfig;
import org.hbrs.thesis.jdbc_connections.PostgresJDBC;
import org.hbrs.thesis.model.Person;

import com.github.javafaker.Faker;

public class PersonDao {
    private ApplicationConfig applicationConfig;
    private PostgresJDBC postgresJDBC;
    private Faker faker = new Faker();

    public PersonDao() {
        this.applicationConfig = new ApplicationConfig();
        this.postgresJDBC = new PostgresJDBC();
    }

    public List<Person> getAllPersons() throws SQLException {
        String sqlStatement = "SELECT id, firstName, lastName, birthDate, timestamp FROM "
                + applicationConfig.getPostgresTable();
        List<Person> persons = new ArrayList<>();
        try (Connection connection = postgresJDBC.createPostgresConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                persons.add(new Person(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getDate(4), resultSet.getTimestamp(5)));
            }
        }
        return persons;
    }

    public Person getPersonById(long id) throws SQLException {
        String sqlStatement = "SELECT id, firstName, lastName, birthDate, timestamp FROM "
                + applicationConfig.getPostgresTable() + " WHERE id = ?";
        Person person = null;
        try (Connection connection = postgresJDBC.createPostgresConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                person = new Person(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getDate(4), resultSet.getTimestamp(5));
            }
        }
        return person;
    }

    public List<Person> getNumberOfPersons(int numberOfPersons) throws SQLException {
        String sqlStatement = "SELECT id, firstName, lastName, birthDate, timestamp FROM "
                + applicationConfig.getPostgresTable() + " LIMIT " + numberOfPersons;
        List<Person> persons = new ArrayList<>();
        try (Connection connection = postgresJDBC.createPostgresConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                persons.add(new Person(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getDate(4), resultSet.getTimestamp(5)));
            }
        }
        return persons;
    }

    public void insertPerson(Person person) throws SQLException {
        String sqlStatement = "INSERT INTO " + applicationConfig.getPostgresTable()
                + " (firstName, lastName, birthDate, timestamp) VALUES (?, ?, ?, ?)";
        try (Connection connection = postgresJDBC.createPostgresConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(sqlStatement)) {
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setDate(3, person.getBirthDate());
            preparedStatement.setTimestamp(4, new Timestamp(Instant.now().toEpochMilli()));
            preparedStatement.executeUpdate();
        }
    }

    public void generateNumberOfRandomPersonsToDB(long numberOfPersonsToGenerate) throws SQLException {
        String sqlStatement = "INSERT INTO " + applicationConfig.getPostgresTable()
                + " (firstName, lastName, birthDate, timestamp) VALUES (?, ?, ?, ?)";
        try (Connection connection = postgresJDBC.createPostgresConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(sqlStatement)) {
            for (long i = 0; i < numberOfPersonsToGenerate; ++i) {
                insertToDB(generatePerson(), preparedStatement);
            }
            preparedStatement.executeBatch();
        }
    }

    private Person generatePerson() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        java.util.Date birthUtilDate = faker.date().birthday();
        Date birthDate = new Date(birthUtilDate.getTime());
        Timestamp timestamp = new Timestamp(Instant.now().toEpochMilli());
        return new Person(0, firstName, lastName, birthDate, timestamp);
    }

    private static PreparedStatement insertToDB(Person person, PreparedStatement preparedStatement)
            throws SQLException {
        preparedStatement.setString(1, person.getFirstName());
        preparedStatement.setString(2, person.getLastName());
        preparedStatement.setDate(3, person.getBirthDate());
        preparedStatement.setTimestamp(4, new Timestamp(Instant.now().toEpochMilli()));

        preparedStatement.addBatch();
        return preparedStatement;
    }

    public void dropDBTable() throws SQLException {
        String sqlStatement = "DROP TABLE " + applicationConfig.getPostgresTable();
        try (Connection connection = postgresJDBC.createPostgresConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(sqlStatement)) {
            preparedStatement.executeUpdate();
        }
    }

    public void updatePerson(Person person) throws SQLException {
        String sqlStatement = "UPDATE " + applicationConfig.getPostgresTable()
                + " SET firstName = ?, lastName = ?, birthDate = ? WHERE id = " + person.getId();
        try (Connection connection = postgresJDBC.createPostgresConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(sqlStatement)) {
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setDate(3, person.getBirthDate());
            preparedStatement.executeUpdate();
        }
    }
}
