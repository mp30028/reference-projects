package com.zonesoft.reference.auth_server.hello_world;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloWorldAuthServerApp {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldAuthServerApp.class);
	
	public static void main(String[] args) {
		
		SpringApplication.run(HelloWorldAuthServerApp.class, args);
		LOGGER.debug("Application Started with args={}", Arrays.toString(args));
	}

}
