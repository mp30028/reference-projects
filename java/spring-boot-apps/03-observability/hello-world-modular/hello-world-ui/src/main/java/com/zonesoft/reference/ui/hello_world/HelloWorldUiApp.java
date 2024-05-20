package com.zonesoft.reference.ui.hello_world;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloWorldUiApp {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldUiApp.class);
	
	public static void main(String[] args) {
		
		SpringApplication.run(HelloWorldUiApp.class, args);
		LOGGER.debug("Application Started with args={}", Arrays.toString(args));
	}

}
