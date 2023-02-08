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

import org.hbrs.thesis.jdbc_connections.PostgresJDBC;
import org.hbrs.thesis.model.Person;

import com.github.javafaker.Faker;

public class PersonDao {

    public static void insertPersonsToDB(long numberOfPersonsToGenerate) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Random random = new Random();
        Faker faker = new Faker();
        String sqlStatement = "INSERT INTO public.persons (firstName, lastName, age, date) VALUES (?, ?, ?, ?)";
        try {
            connection = new PostgresJDBC().createPostgresConnection();
            preparedStatement = connection.prepareStatement(sqlStatement);
            for (long i = 0; i < numberOfPersonsToGenerate; ++i) {
                insertToDB(generatePerson(faker, random), preparedStatement);
            }
            preparedStatement.executeBatch();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (connection != null)
                connection.close();
        }
    }

    private static Person generatePerson(Faker faker, Random random) {
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

    public static List<Person> getAllPersonsFromDb() throws SQLException {
        String sqlStatement = "SELECT id, firstName, lastName, age, date FROM public.persons";
        List<Person> persons = new ArrayList<>();
        try (PreparedStatement preparedStatement = new PostgresJDBC().createPostgresConnection()
                .prepareStatement(sqlStatement)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                persons.add(new Person(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4), resultSet.getString(5)));
            }
        }
        return persons;
    }

    public static void dropPersonsTable() throws SQLException {
        String sqlStatement = "DROP TABLE persons";
        try (PreparedStatement preparedStatement = new PostgresJDBC().createPostgresConnection()
                .prepareStatement(sqlStatement)) {
            preparedStatement.executeUpdate();
        }
    }
}
