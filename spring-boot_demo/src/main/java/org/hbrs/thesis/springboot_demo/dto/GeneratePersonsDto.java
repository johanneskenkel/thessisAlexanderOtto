package org.hbrs.thesis.springboot_demo.dto;

public class GeneratePersonsDto {
    private Long numberOfPersons;

    public GeneratePersonsDto() {
    }

    public GeneratePersonsDto(Long numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public long getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(Long numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

}
