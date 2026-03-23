package com.ortiz.tpv_backend;

import com.ortiz.tpv_backend.infrastructure.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ConfigurationPropertiesScan
@Import(SecurityConfig.class)
public class TpvBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpvBackendApplication.class, args);
	}

}
