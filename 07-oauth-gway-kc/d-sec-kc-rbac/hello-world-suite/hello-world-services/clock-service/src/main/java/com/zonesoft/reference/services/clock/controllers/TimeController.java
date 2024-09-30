package com.zonesoft.reference.services.clock.controllers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value={"/hello-world/api/clock"})
public class TimeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeController.class);
	
	@GetMapping(value={"/get-time"})
	@ResponseBody
	public ResponseEntity<String> getTime() {
		Instant gmtInstant = Instant.now();
		String PATTERN_FORMAT = "HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone(ZoneId.of("UTC"));
        String formattedInstant = formatter.format(gmtInstant);	
        LOGGER.debug("getTime() invoked. gmtInstant={}, PATTERN_FORMAT={}, formattedInstant={})", gmtInstant, PATTERN_FORMAT, formattedInstant);
		return ResponseEntity.ok(formattedInstant);
	}
}