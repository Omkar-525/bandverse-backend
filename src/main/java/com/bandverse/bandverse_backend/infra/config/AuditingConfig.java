package com.bandverse.bandverse_backend.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    private static final ZoneId APPLICATION_ZONE =
            ZoneId.of("Asia/Kolkata");

    @Bean
    public Clock applicationClock() {
        return Clock.system(APPLICATION_ZONE);
    }
}