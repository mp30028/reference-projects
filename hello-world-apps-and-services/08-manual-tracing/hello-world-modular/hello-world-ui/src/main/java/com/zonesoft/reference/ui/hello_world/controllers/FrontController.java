package com.zonesoft.reference.ui.hello_world.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zonesoft.reference.ui.hello_world.services.GreetingService;


@Controller
public class FrontController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);
	private final GreetingService greetingService;

	@Autowired
	public FrontController(GreetingService greetingService) {
		super();
		this.greetingService = greetingService;
	}
	
	@GetMapping(value = { "show-greeting" })
	@ResponseBody
	public String greeting() {
		LOGGER.debug("Request to show greting received");
		String serviceMessage = greetingService.invoke();
		LOGGER.debug("Greeting Service invoked and returned the following message:-{}", serviceMessage);
		StringBuilder htmlResponse = new StringBuilder();
		htmlResponse.append("<h3>Here is the message from greeting service</h3>");
		htmlResponse.append("<h4>");
		htmlResponse.append(serviceMessage);
		htmlResponse.append("</h4>");
		htmlResponse.append("<br/><br/>");
		return htmlResponse.toString();
	}	
}