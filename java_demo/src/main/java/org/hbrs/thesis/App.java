package org.hbrs.thesis;

import static spark.Spark.exception;
import static spark.Spark.port;
import static spark.Spark.before;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.hbrs.thesis.config.ApplicationConfig;
import org.hbrs.thesis.controller.MetricsController;
import org.hbrs.thesis.controller.PersonController;
import org.hbrs.thesis.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public final class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private App() {
    }

    /**
     * Generates 3 Person endpoints
     * 
     * @param args The arguments of the program.
     * @throws IOException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws ExecutionException
     */

    public static void main(String[] args)
            throws IOException {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        port(applicationConfig.getApplicationPort());
        before((req, res) -> res.header("Content-Type", "application/json"));
        PersonController personController = new PersonController();
        personController.generateRandomPersons();
        personController.getPersonEndpoints();
        personController.removeTable();
        personController.insertPerson();
        personController.updatePerson();
        personController.randomCaclulation();
        MetricsController metricsController = new MetricsController();
        metricsController.exposePrometheusMetrics();
        Gson gson = new Gson();
        exception(Exception.class, (exception, req, res) -> {
            res.type("application/json");
            res.status(400);
            MessageDto messageDto = new MessageDto("Something went wrong");
            res.body(gson.toJson(messageDto));
            logger.warn("There was an exception: {}, that was thrown with the message: {}",
                    exception.getClass().getSimpleName(), exception.getLocalizedMessage());
        });
    }
}
