package org.hbrs.thesis.springboot_demo.dto;

public class GeneratePersonsDto {
    private long numberOfPersons;

    public GeneratePersonsDto() {
    }

    public GeneratePersonsDto(long numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public long getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(long numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

}
