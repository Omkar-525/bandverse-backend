package com.bandverse.bandverse_backend.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(
        dateTimeProviderRef = "auditingDateTimeProvider"
)
public class AuditingConfig {

    private static final String APPLICATION_TIME_ZONE = "Asia/Kolkata";

    @Bean
    public Clock applicationClock() {
        return Clock.system(
                java.time.ZoneId.of(APPLICATION_TIME_ZONE)
        );
    }

    @Bean
    public DateTimeProvider auditingDateTimeProvider(
            Clock applicationClock
    ) {
        return () -> Optional.of(
                OffsetDateTime.now(applicationClock)
        );
    }
}