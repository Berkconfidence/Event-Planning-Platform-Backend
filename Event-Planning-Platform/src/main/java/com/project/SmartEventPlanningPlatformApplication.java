package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
//@EntityScan(basePackages = "project.entities")
@EntityScan(basePackages = {"com.project.entities"})
@CrossOrigin(origins = "http://localhost:3000") // Frontend URL'ini buraya yaz
public class SmartEventPlanningPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartEventPlanningPlatformApplication.class, args);
	}


}
