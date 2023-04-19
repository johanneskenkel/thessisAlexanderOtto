package org.hbrs.thesis;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.hbrs.thesis.config.ApplicationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spark.Spark;

/**
 * Unit test for simple App.
 */
class AppTest {
    ApplicationConfig applicationConfig;

    @BeforeEach
    void setup() {
        applicationConfig = new ApplicationConfig();
    }

    @Test
    void assureThatAppRunsCorrectly() throws IOException, InterruptedException {
        App.main(null);
        Spark.awaitInitialization();
        int statusCode = 0;
        URL url = new URL(
                applicationConfig.getApplicationHost() + ":" + applicationConfig.getApplicationPort() + "/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        statusCode = con.getResponseCode();

        assertTrue(statusCode < 500 && statusCode >= 200);
        Spark.stop();
    }
}
