package com.parceiroferramentas.api.parceiro_api.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseConfig {

    public static PostgreSQLContainer<?> getDatabaseConfig() {
        return new PostgreSQLContainer<>("postgres:18-alpine")
        .withDatabaseName("testdb")
        .withUsername("postgres")
        .withPassword("1234");
    }
}
