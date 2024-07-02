package com.zonesoft.reference.ui.hello_world.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@GetMapping(value={"","/", "/home"})
	@ResponseBody
	public String home( ) {
		LOGGER.debug("Request for static content received");
		StringBuilder htmlResponse = new StringBuilder();
		htmlResponse.append("<h3>");
			htmlResponse.append("Welcome to Greetings Apps & Services Home ");
			htmlResponse.append("<h4>");
				htmlResponse.append("Select what you would like to do next ");
			htmlResponse.append("</h4>");
			htmlResponse.append("<a href=\"/show-user-greeting\">Continue as user </a><br/>");
			htmlResponse.append("<a href=\"/show-admin-greeting\">Continue as admin </a><br/>");
		htmlResponse.append("</h3>");
		return htmlResponse.toString();
	}

	@GetMapping(value={"/signed-out"})
	@ResponseBody
	public String signedOut() {
		LOGGER.debug("Request to SIGN-OUT received");
		StringBuilder htmlResponse = new StringBuilder();			
			htmlResponse.append("<h3>");
				htmlResponse.append("Bye");
				htmlResponse.append("<h4>");
					htmlResponse.append("You have successfully logged out of Hello-World-Modular");
				htmlResponse.append("</h4>");
				htmlResponse.append("<a href=\"/home\">Continue (Home)</a><br/>");
		return htmlResponse.toString();
	}	
	
	
}