package org.hbrs.thesis;

import static spark.Spark.port;

import org.hbrs.thesis.config.ApplicationConfig;
import org.hbrs.thesis.controller.PersonController;
public final class App {
    private App() {
    }

    /**
     * Generates 3 Person endpoints
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        port(applicationConfig.getApplicationPort());
        PersonController personController = new PersonController();
        personController.generatePersons();
        personController.getAllPersons();
        personController.removeTable();
    }
}
