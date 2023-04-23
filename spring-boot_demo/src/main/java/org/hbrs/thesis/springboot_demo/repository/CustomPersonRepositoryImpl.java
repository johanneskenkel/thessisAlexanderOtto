package org.hbrs.thesis.springboot_demo.repository;

import java.sql.Timestamp;
import java.time.Instant;

import org.hbrs.thesis.springboot_demo.model.Person;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
public class CustomPersonRepositoryImpl implements CustomPersonRepository{
  @PersistenceContext
  private EntityManager entityManager;
  @Transactional
  public void insertPersonToDb(Person person) {
    entityManager
        .createNativeQuery("INSERT INTO persons (firstName, lastName, birthDate, timestamp) VALUES (?, ?, ?, ?)")
        .setParameter(1, person.getFirstName()).setParameter(2, person.getLastName())
        .setParameter(3, person.getBirthDate()).setParameter(4, new Timestamp(Instant.now().toEpochMilli())).executeUpdate();
  }
  @Transactional
  public void dropDBTable() {
    entityManager.createNativeQuery("DROP TABLE persons").executeUpdate();
  }

}
