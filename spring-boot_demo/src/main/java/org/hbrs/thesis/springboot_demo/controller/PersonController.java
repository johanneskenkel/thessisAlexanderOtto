package org.hbrs.thesis.springboot_demo.controller;

import java.util.List;

import org.hbrs.thesis.springboot_demo.dto.GeneratePersonsDto;
import org.hbrs.thesis.springboot_demo.dto.MessageDto;
import org.hbrs.thesis.springboot_demo.dto.PersonDto;
import org.hbrs.thesis.springboot_demo.mappings.PersonMappings;
import org.hbrs.thesis.springboot_demo.service.PersonService;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
  private PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping(value={"", "/"})
  public ResponseEntity<List<PersonDto>> getPersons(@RequestParam(required = false) Integer numberOfPersons) {
    if(numberOfPersons != null) {
    return ResponseEntity.ok()
        .body(PersonMappings.mapPersonListToPersonDtoList(this.personService.getNumberOfPersons(numberOfPersons)));
    }
   return ResponseEntity.ok().body(PersonMappings.mapPersonListToPersonDtoList(this.personService.getAllPersons()));
  }


  @GetMapping("/{id}")
  public ResponseEntity<PersonDto> getPersonById(@PathVariable Long id) throws NotFoundException {
    return ResponseEntity.ok().body(PersonMappings.mapPersonToPersonDto(this.personService.getPersonById(id)));
  }

  @PostMapping("/generate")
  public ResponseEntity<MessageDto> generatePersons(@RequestBody GeneratePersonsDto generatePersonsDto) {
    return ResponseEntity.ok().body(this.personService.generatePersonsToDB(generatePersonsDto.getNumberOfPersons()));
  }

  @DeleteMapping("/delete/deleteAll")
  public ResponseEntity<MessageDto> removeTable() {
    return ResponseEntity.ok().body(this.personService.removeDBTable());
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<MessageDto> removePerson(@PathVariable Long id) {
    return ResponseEntity.ok().body(this.personService.removePersonById(id));
  }
}
