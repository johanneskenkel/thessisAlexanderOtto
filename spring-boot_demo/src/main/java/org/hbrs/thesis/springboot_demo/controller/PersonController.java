package org.hbrs.thesis.springboot_demo.controller;

import org.hbrs.thesis.springboot_demo.dto.GeneratePersonsDto;
import org.hbrs.thesis.springboot_demo.dto.MessageDto;
import org.hbrs.thesis.springboot_demo.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
  private PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping("/")
  public ResponseEntity<Object> getAllPersons() {
    return null;
  }

  @PostMapping("/generate")
  public ResponseEntity<MessageDto> generatePersons(@RequestBody GeneratePersonsDto generatePersonsDto) {
    return ResponseEntity.ok().body(this.personService.generatePersonsToDB(generatePersonsDto.getNumber()));
  }
}
