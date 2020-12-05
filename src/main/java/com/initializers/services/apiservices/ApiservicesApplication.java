package com.initializers.services.apiservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan({"com.initializers.services.apiservices"})
@EnableAsync
public class ApiservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiservicesApplication.class, args);
	}

}
