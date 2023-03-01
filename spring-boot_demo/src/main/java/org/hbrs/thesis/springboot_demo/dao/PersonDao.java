package org.hbrs.thesis.springboot_demo.dao;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hbrs.thesis.springboot_demo.model.Person;
import org.hbrs.thesis.springboot_demo.repository.PersonRepository;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;

@Service
public class PersonDao {
  private static Random random = new Random();
  private Faker faker = new Faker();
  private PersonRepository personRepository;

  public PersonDao(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public void insertNumberOfRandomPersonsToDB(long numberOfPersonsToGenerate) {
    List<Person> personList = new ArrayList<>();
    for(int i=0; i<numberOfPersonsToGenerate; ++i) {
      personList.add(generatePerson());
    }
    personRepository.saveAll(personList);
  }

  private Person generatePerson() {
    String firstName = faker.name().firstName();
    String lastName = faker.name().lastName();
    int age = 18 + random.nextInt(83);
    String timestamp = Instant.now().toString();
    return new Person(0, firstName, lastName, age, timestamp);
}

}
