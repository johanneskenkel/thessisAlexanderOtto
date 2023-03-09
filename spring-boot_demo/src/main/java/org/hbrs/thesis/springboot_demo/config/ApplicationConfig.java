package org.hbrs.thesis.springboot_demo.config;

import org.springframework.beans.factory.annotation.Value;

public class ApplicationConfig {
    @Value("postgres.table")
    private String postgresTable;

    public String getPostgresTable() {
        return postgresTable;
    }
}
