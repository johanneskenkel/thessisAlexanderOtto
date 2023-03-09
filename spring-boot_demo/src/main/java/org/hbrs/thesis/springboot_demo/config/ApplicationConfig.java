package org.hbrs.thesis.springboot_demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Value("postgres.table")
    private String postgresTable;

    public String getPostgresTable() {
        return postgresTable;
    }
}
