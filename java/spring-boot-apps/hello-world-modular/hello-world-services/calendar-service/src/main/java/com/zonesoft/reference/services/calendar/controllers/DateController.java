package com.zonesoft.reference.services.calendar.controllers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController

public class DateController {
	
	@GetMapping(value={"/get-date"})
	@ResponseBody
	public ResponseEntity<String> getDate() {
		Instant gmtInstant = Instant.now();
		String PATTERN_FORMAT = "dd-MMM-yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone(ZoneId.of("UTC"));
        String formattedInstant = formatter.format(gmtInstant);		
		return ResponseEntity.ok(formattedInstant);
	}
}