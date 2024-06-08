package com.zonesoft.reference.ui.hello_world.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zonesoft.reference.ui.hello_world.services.GreetingClientService;


@Controller
public class FrontController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);
	private final GreetingClientService greetingClientService;

	@Autowired
	public FrontController(GreetingClientService greetingClientService) {
		super();
		this.greetingClientService = greetingClientService;
	}
	
	@GetMapping(value = { "show-greeting-1" })
	@ResponseBody
	public String greeting_1() {
		LOGGER.debug("Request to show greeting one received");
		String serviceMessage = greetingClientService.invoke();
		LOGGER.debug("Greeting Service invoked and returned the following message:-{}", serviceMessage);
		StringBuilder htmlResponse = new StringBuilder();
		htmlResponse.append("<h3>Here is the message from greeting service 1</h3>");
		htmlResponse.append("<h4>");
		htmlResponse.append(serviceMessage);
		htmlResponse.append("</h4>");
		htmlResponse.append("<br/><br/>");
		return htmlResponse.toString();
	}
	
	@GetMapping(value = { "show-greeting-2" })
	@ResponseBody
	public String greeting_2(@RequestParam(name="name", required = false, defaultValue = "World") String name) {
		LOGGER.debug("Request to show greting two received from someone called {}", name);
		String serviceMessage = greetingClientService.invoke();
		LOGGER.debug("Greeting Service invoked and returned the following message:-{}", serviceMessage);
		StringBuilder htmlResponse = new StringBuilder();
		htmlResponse.append("<h3>Here is the message from greeting service 2</h3>");
		htmlResponse.append("<h4>");
		htmlResponse.append(serviceMessage);
		htmlResponse.append("<br>");
		htmlResponse.append("The requestor was identified as "); htmlResponse.append(name);
		htmlResponse.append("</h4>");
		htmlResponse.append("<br/><br/>");
		return htmlResponse.toString();
	}		
}