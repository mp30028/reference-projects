package com.zonesoft.reference.services.clock.controllers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController

public class TimeController {
	
	@GetMapping(value={"/get-time"})
	@ResponseBody
	public ResponseEntity<String> getTime() {
		Instant gmtInstant = Instant.now();
		String PATTERN_FORMAT = "HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone(ZoneId.of("UTC"));
        String formattedInstant = formatter.format(gmtInstant);		
		return ResponseEntity.ok(formattedInstant);
	}
}