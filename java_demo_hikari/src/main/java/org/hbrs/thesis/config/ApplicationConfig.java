package org.hbrs.thesis.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {
    private Properties applicationProperties;

    public ApplicationConfig() {
        this.applicationProperties = getRessourceFileProperties();
    }

    private Properties getRessourceFileProperties() {
        ClassLoader classLoader = getClass().getClassLoader();
        String applicationPropsFileName = "application.properties";
        // the stream holding the file content
        Properties properties = new Properties();
        try (InputStream inputStream = classLoader.getResourceAsStream(applicationPropsFileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public int getApplicationPort() {
        return Integer.parseInt( applicationProperties.getProperty("application.port"));
    }

    public String getApplicationHost() {
        return applicationProperties.getProperty("application.host");

    }
    public String getPostgresUrl() {
        return applicationProperties.getProperty("postgres.url");
    }

    public String getPostgresUsername() {
        return applicationProperties.getProperty("postgres.username");
    }

    public String getPostgresPassword() {
        return applicationProperties.getProperty("postgres.password");
    }

    public String getPostgresTable() {
        return applicationProperties.getProperty("postgres.table");
    }

}
