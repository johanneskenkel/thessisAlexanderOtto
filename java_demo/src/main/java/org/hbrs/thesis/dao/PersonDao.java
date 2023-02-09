package org.hbrs.thesis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hbrs.thesis.config.ApplicationConfig;
import org.hbrs.thesis.jdbc_connections.PostgresJDBC;
import org.hbrs.thesis.model.Person;

import com.github.javafaker.Faker;

public class PersonDao {
    private static Random random = new Random();
    private ApplicationConfig applicationConfig;
    PostgresJDBC postgresJDBC;

    public PersonDao() {
        this.applicationConfig = new ApplicationConfig();
        this.postgresJDBC = new PostgresJDBC();
    }

    public void insertNumberOfRandomPersonsToDB(long numberOfPersonsToGenerate) throws SQLException {
        Faker faker = new Faker();
        String sqlStatement = "INSERT INTO " + applicationConfig.getPostgresTable() + " (firstName, lastName, age, date) VALUES (?, ?, ?, ?)";
        try (Connection connection = postgresJDBC.createPostgresConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                for (long i = 0; i < numberOfPersonsToGenerate; ++i) {
                    insertToDB(generatePerson(faker, random), preparedStatement);
                }
                preparedStatement.executeBatch();
            }
        }
    }

    private Person generatePerson(Faker faker, Random random) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        int age = 18 + random.nextInt(83);
        return new Person(0, firstName, lastName, age, null);
    }

    private static PreparedStatement insertToDB(Person person, PreparedStatement preparedStatement)
            throws SQLException {
        preparedStatement.setString(1, person.getFirstName());
        preparedStatement.setString(2, person.getLastName());
        preparedStatement.setInt(3, person.getAge());
        preparedStatement.setTimestamp(4, Timestamp.from(Instant.now()));
        preparedStatement.addBatch();
        return preparedStatement;
    }

    public List<Person> getAllFromDb() throws SQLException {
        String sqlStatement = "SELECT id, firstName, lastName, age, date FROM " + applicationConfig.getPostgresTable();
        List<Person> persons = new ArrayList<>();
        try (PreparedStatement preparedStatement = postgresJDBC.createPostgresConnection()
                .prepareStatement(sqlStatement)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                persons.add(new Person(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4), resultSet.getString(5)));
            }
        }
        return persons;
    }

    public void dropDBTable() throws SQLException {
        String sqlStatement = "DROP TABLE " + applicationConfig.getPostgresTable();
        try (PreparedStatement preparedStatement = postgresJDBC.createPostgresConnection()
                .prepareStatement(sqlStatement)) {
            preparedStatement.executeUpdate();
        }
    }
}
