package com.zonesoft.reference.services.greeting.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.zonesoft.reference.services.greeting.services.DateService;
import com.zonesoft.reference.services.greeting.services.TimeService;


@RestController
public class GreetingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);
	private final DateService dateService;
	private final TimeService timeService;

	
	@Autowired
	public GreetingController(DateService dateService, TimeService timeService) {
		super();
		this.dateService = dateService;
		this.timeService = timeService;
	}	

	@GetMapping(value={"/greeting"})
	@ResponseBody
	public ResponseEntity<String> greeting() {
		String resultDate = this.dateService.invoke();
		String resultTime = this.timeService.invoke();	
		LOGGER.debug("greeting invoked at {} on {}", resultTime, resultDate);
		return ResponseEntity.ok("Hello from greeting service on " + resultDate + " at GMT " + resultTime);
	}		
}