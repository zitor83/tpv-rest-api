package com.ortiz.tpv_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TpvBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpvBackendApplication.class, args);
	}

}
