package org.hbrs.thesis.utils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.hbrs.thesis.model.Person;
import com.github.javafaker.Faker;

public class PersonService {

    public static List<Person> generateMillionPersons() {
        Faker faker = new Faker();
        List<Person> persons = new ArrayList<>();
        Random random = new Random();

        for(int i=0; i<1e6; ++i) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            int age = 18 + random.nextInt(83);
            String addDate = DateFormat.getDateInstance().format(new Date());
            persons.add(new Person(i, firstName, lastName, age, addDate));
        }
        return persons;
    }
}
