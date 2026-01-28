package com.parceiroferramentas.api.parceiro_api.health;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public @Nullable Health health() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Health.up()
                .withDetail("database", "PostgreSQL 18")
                .withDetail("status", "Conexão OK")
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("database", "PostgreSQL 18")
                .withDetail("error", e.getMessage())
                .withException(e)
                .build();
        }
    }

}
