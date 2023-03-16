package org.hbrs.thesis.springboot_demo.dto;

import java.sql.Timestamp;
import java.sql.Date;

public class PersonDto {
    private long id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Timestamp timestamp;

    public PersonDto(long id, String firstName, String lastName, Date birthDate, Timestamp timestamp) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getAge() {
        return birthDate;
    }

    public void setAge(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
