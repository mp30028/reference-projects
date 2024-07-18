package com.zonesoft.reference.datetime.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    
	@GetMapping(value={"","/", "/home"})
	@ResponseBody
	public String home() {
		LOGGER.debug("Request for home received");
		return "Welcome to Date-Time Services";
	}    
}
