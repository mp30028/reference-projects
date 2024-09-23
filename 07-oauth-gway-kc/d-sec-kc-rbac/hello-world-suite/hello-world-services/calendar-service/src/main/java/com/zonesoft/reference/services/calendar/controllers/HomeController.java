package com.zonesoft.reference.services.calendar.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/hello-world/api/calendar"})
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    
	@GetMapping(value={"","/", "/home"})
	@ResponseBody
	public String home() {
		LOGGER.debug("Request for home received");
		return "Welcome to the Calendar Service";
	}    
}
