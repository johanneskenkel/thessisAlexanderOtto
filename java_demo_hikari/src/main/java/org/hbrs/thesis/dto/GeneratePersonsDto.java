package org.hbrs.thesis.dto;

public class GeneratePersonsDto {
    private Long numberOfPersons;

    public GeneratePersonsDto(Long numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public Long getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(Long numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

}
