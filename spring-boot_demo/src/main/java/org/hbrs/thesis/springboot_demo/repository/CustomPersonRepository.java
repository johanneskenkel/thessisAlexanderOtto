package org.hbrs.thesis.springboot_demo.repository;

import org.hbrs.thesis.springboot_demo.model.Person;

public interface CustomPersonRepository {
  void insertPersonToDb(Person person);
  void dropDBTable();
}
