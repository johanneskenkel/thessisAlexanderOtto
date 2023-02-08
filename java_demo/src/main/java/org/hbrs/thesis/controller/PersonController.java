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
    private PersonController() {
    }

    public static void getAllPersons() {
        Gson gson = new Gson();
        get("/api/persons", (req, res) -> PersonMappings.mapPersonListToPersonDtoList(PersonService.getAllPersons()),
                gson::toJson);
    }

    public static void generatePersons() {
        Gson gson = new Gson();
        post("/api/persons/generate", (req, res) -> {
            GeneratePersonsDto generatePersonsDto = gson.fromJson(req.body(), GeneratePersonsDto.class);
            return PersonService.generatePersons(generatePersonsDto.getNumber());
        }, gson::toJson);
        exception(JsonSyntaxException.class, (exception, req, res) -> {
            res.type("application/json");
            res.status(400);
            MessageDto messageDto = new MessageDto(
                    "Could not parse String to Long with the exception message: " + exception.getLocalizedMessage());
            res.body(gson.toJson(messageDto));
        });
    }

    public static void removeTable() {
        Gson gson = new Gson();
        delete("/api/persons/delete", (req, res) -> gson.toJson(PersonService.removePersonTable()));
    }
}
