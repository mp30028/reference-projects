package com.zonesoft.reference.services.calendar.tryouts;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Tryout {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Tryout.class);	
	
	@Test
	void test() {
		Instant gmtInstant = Instant.now();
		LOGGER.debug("gmtInstant={}", gmtInstant);
		String DATE_FORMAT = "dd-MMM-yyyy";
		String TIME_FORMAT = "HH:mm:ss";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(ZoneId.of("UTC"));
        String formattedDate = dateFormatter.format(gmtInstant);		
		LOGGER.debug("DATE >> {}", formattedDate);
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT).withZone(ZoneId.of("UTC"));
        String formattedTime = timeFormatter.format(gmtInstant);		
		LOGGER.debug("TIME >> {}", formattedTime);
	}

	
	
}
