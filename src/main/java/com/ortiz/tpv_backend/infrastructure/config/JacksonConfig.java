package com.ortiz.tpv_backend.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Jackson para soportar tipos de Java 8 Date/Time (java.time.*)
 * 
 * Sin esta configuración, Jackson no puede serializar/deserializar tipos como:
 * - java.time.Instant
 * - java.time.LocalDate
 * - java.time.LocalDateTime
 * - etc.
 * 
 * Esta configuración registra el módulo JavaTimeModule de forma global.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}

