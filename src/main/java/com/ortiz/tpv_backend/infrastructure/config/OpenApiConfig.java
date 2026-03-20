package com.ortiz.tpv_backend.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tpvOpenApi(@Value("${spring.application.name:tpv-backend}") String applicationName) {
        return new OpenAPI()
            .info(new Info()
                .title(applicationName)
                .description("API backend del TPV")
                .version("v1"));
    }
}


