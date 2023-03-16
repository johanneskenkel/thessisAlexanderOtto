package org.hbrs.thesis.springboot_demo.mappings;

import java.util.ArrayList;
import java.util.List;

import org.hbrs.thesis.springboot_demo.dto.PersonDto;
import org.hbrs.thesis.springboot_demo.model.Person;


public class PersonMappings {
    private PersonMappings() {}
    public static List<PersonDto> mapPersonListToPersonDtoList(List<Person> personList) {
        List<PersonDto> personDtoList = new ArrayList<>();
        personList.forEach(person -> personDtoList.add(mapPersonToPersonDto(person)));
        return personDtoList;
    }

    public static PersonDto mapPersonToPersonDto(Person person) {
        return new PersonDto(person.getId(), person.getFirstName(), person.getLastName(), person.getBirthDate(), person.getTimestamp());
    }

    public static Person mapPersonDtoToPerson(PersonDto personDto) {
        return new Person(personDto.getId(), personDto.getFirstName(), personDto.getLastName(), personDto.getAge(), personDto.getTimestamp());
    }

}
