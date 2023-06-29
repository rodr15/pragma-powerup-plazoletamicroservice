package com.ti.acelera.plazoletamicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlazoletamicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlazoletamicroserviceApplication.class, args);
	}

}
