package com.zonesoft.reference.services.greeting;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GreetingServiceApp {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GreetingServiceApp.class);

	public static void main(String[] args) {
		SpringApplication.run(GreetingServiceApp.class, args);
		LOGGER.debug("Application Started with args={}", Arrays.toString(args));
	}

}
