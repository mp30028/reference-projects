package com.zonesoft.reference.ui.hello_world;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloWorldUiApp {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldUiApp.class);
	
	public static void main(String[] args) {
		LOGGER.debug("Application Started with args={}", args.toString());
		SpringApplication.run(HelloWorldUiApp.class, args);
	}

}
