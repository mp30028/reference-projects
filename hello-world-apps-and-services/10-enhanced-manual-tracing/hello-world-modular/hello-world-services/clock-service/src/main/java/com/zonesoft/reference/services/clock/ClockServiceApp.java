package com.zonesoft.reference.services.clock;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ClockServiceApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClockServiceApp.class);
	public static void main(String[] args) {
		SpringApplication.run(ClockServiceApp.class, args);
		LOGGER.debug("Application Started with args={}", Arrays.toString(args));
	}

}
