package com.zonesoft.reference.services.calendar.controllers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zonesoft.reference.utils.DoNothing;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@RestController

public class DateController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DateController.class);
	
	private final Counter countOfTotalGetDateRequests;
	private final Timer timeTakenToProcessGetDateRequest;
	
	public DateController(MeterRegistry registry) {
		
		this.countOfTotalGetDateRequests = Counter
				.builder("getDate_invoked_total")
				.tag("request", "get-date")
				.description("Total number of times get-date api was called")
				.register(registry);
		
		this.timeTakenToProcessGetDateRequest = Timer
				.builder("getDate_processing_time")
				.tag("request", "get-date")
				.description("Time taken to process a get-date api call")
				.register(registry);
	}
	
	
	@GetMapping(value={"/get-date"})
	@ResponseBody
	public ResponseEntity<String> getDate() {
		long start = System.currentTimeMillis();
			countOfTotalGetDateRequests.increment();
			Instant gmtInstant = Instant.now();
			String PATTERN_FORMAT = "dd-MMM-yyyy";
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone(ZoneId.of("UTC"));
	        String formattedInstant = formatter.format(gmtInstant);
	        LOGGER.debug("getDate() invoked. gmtInstant={}, PATTERN_FORMAT={}, formattedInstant={})", gmtInstant, PATTERN_FORMAT, formattedInstant);
	        DoNothing.pretendDoingSomething();
        timeTakenToProcessGetDateRequest.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
		return ResponseEntity.ok(formattedInstant);
	}
	
}