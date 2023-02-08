package org.hbrs.thesis.dto;

public class GeneratePersonsDto {
    private Long number;

    public GeneratePersonsDto(Long number) {
        this.number = number;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

}
