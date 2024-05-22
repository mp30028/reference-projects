package com.zonesoft.reference.services.calendar.controllers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zonesoft.reference.utils.DoNothing;

@RestController

public class DateController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DateController.class);
//	private static final int MINIMUM_WAIT_MS =30;
//	private static final int MAXIMUM_WAIT_MS = 2100;
	
	@GetMapping(value={"/get-date"})
	@ResponseBody
	public ResponseEntity<String> getDate() {
		Instant gmtInstant = Instant.now();
		String PATTERN_FORMAT = "dd-MMM-yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone(ZoneId.of("UTC"));
        String formattedInstant = formatter.format(gmtInstant);
        LOGGER.debug("getDate() invoked. gmtInstant={}, PATTERN_FORMAT={}, formattedInstant={})", gmtInstant, PATTERN_FORMAT, formattedInstant);
        DoNothing.pretendDoingSomething();;
		return ResponseEntity.ok(formattedInstant);
	}
	
//	private void pretendDoingSomething() {
//        try {
//        	int waitMilliseconds = new Random().nextInt(MAXIMUM_WAIT_MS - MINIMUM_WAIT_MS + 1) + MINIMUM_WAIT_MS;
//        	LOGGER.debug("Starting waitMilliseconds={}", waitMilliseconds);
//            Thread.sleep(waitMilliseconds);
//            LOGGER.debug("Finished waitMilliseconds={}", waitMilliseconds);
//        } catch (InterruptedException e) {
//            System.err.format("IOException: %s%n", e);
//        }
//	}
}