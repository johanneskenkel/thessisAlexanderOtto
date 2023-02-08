package org.hbrs.thesis;

import org.hbrs.thesis.controller.PersonController;
import static spark.Spark.port;
public final class App {
    private App() {
    }

    /**
     * Generates 3 Person endpoints
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        port(8081);
        PersonController.generatePersons();
        PersonController.getAllPersons();
        PersonController.removeTable();
    }
}
