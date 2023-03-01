package org.hbrs.thesis.springboot_demo.dto;

public class GeneratePersonsDto {
    private long number;

    public GeneratePersonsDto() {
    }

    public GeneratePersonsDto(long number) {
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

}
