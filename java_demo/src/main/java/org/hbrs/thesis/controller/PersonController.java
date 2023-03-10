package org.hbrs.thesis.controller;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

import org.hbrs.thesis.dto.GeneratePersonsDto;
import org.hbrs.thesis.dto.MessageDto;
import org.hbrs.thesis.mappings.PersonMappings;
import org.hbrs.thesis.service.PersonService;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class PersonController {
    private PersonService personService;

    public PersonController() {
        this.personService = new PersonService();
    }

    public void getAllPersons() {
        Gson gson = new Gson();
        get("/api/persons", (req, res) -> {
            return PersonMappings.mapPersonListToPersonDtoList(personService.getAllPersons());
        },
                gson::toJson);

    }

    public void generatePersons() {
        Gson gson = new Gson();
        post("/api/persons/generate", (req, res) -> {
            GeneratePersonsDto generatePersonsDto = gson.fromJson(req.body(), GeneratePersonsDto.class);
            return personService.generatePersonsToDB(generatePersonsDto.getNumberOfPersons());
        }, gson::toJson);
        exception(JsonSyntaxException.class, (exception, req, res) -> {
            res.type("application/json");
            res.status(400);
            MessageDto messageDto = new MessageDto(
                    "Could not parse String to Long with the exception message: " + exception.getLocalizedMessage());
            res.body(gson.toJson(messageDto));
        });
    }

    public void removeTable() {
        Gson gson = new Gson();
        delete("/api/persons/deleteAll", (req, res) -> gson.toJson(personService.removeDBTable()));
    }

    public void randomCaclulation() {
        Gson gson = new Gson();
        get("/api/random", (req, res) -> gson.toJson(personService.randomCalculation()));
    }
}
