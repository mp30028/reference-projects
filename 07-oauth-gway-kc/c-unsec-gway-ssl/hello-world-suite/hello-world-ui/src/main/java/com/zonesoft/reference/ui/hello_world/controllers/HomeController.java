package com.zonesoft.reference.ui.hello_world.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller  
@RequestMapping(value={"/hello-world/ui"})
public class HomeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@GetMapping(value={"","/"})
	@ResponseBody
	public String greeting( ) {
		LOGGER.debug("Request for static content received");
//		String username = "mp30028";
//		String fullname = "test user";
//		String info = "This is some info";
		StringBuilder htmlResponse = new StringBuilder();			
			htmlResponse.append("<h3>");
				htmlResponse.append("Welcome");
				htmlResponse.append("<h4>");
					htmlResponse.append("You have successfully accessed the home page");
				htmlResponse.append("</h4>");
			htmlResponse.append("</h3>");
			htmlResponse.append("<br/><br/>");
			htmlResponse.append("<b>");
				htmlResponse.append("About this Application");
			htmlResponse.append("</b>");
			htmlResponse.append("<div style=\"width:800px; overflow:auto\">");
				htmlResponse.append("<pre style=\"white-space: pre-wrap\">");
					htmlResponse.append("This a simple unsecured application\n");
					htmlResponse.append("It consists of a basic UI whose home page you are currently viewing\n");
					htmlResponse.append("From the home page you can trigger functionality that in turn invokes a Greeting Service\n");
					htmlResponse.append("The Greeting Service in turn invokes two other services to get the date and time \n");
					htmlResponse.append("When you trigger the greeting functionality you should see the results on a different page \n");
				htmlResponse.append("</pre>");
			htmlResponse.append("<div/>");
			htmlResponse.append("<a href=\"/hello-world/ui/show-greeting\">Invoke Greeting Service</a><br/><br/><br/>");
			htmlResponse.append("<div style=\"width:1200px; overflow:auto\">");
				htmlResponse.append("<pre style=\"white-space: pre-wrap\">");
					htmlResponse.append("List of URLs to test with and without spring cloud gateway\n\n");
					htmlResponse.append("UI via Gateway                   https://localhost:8210/hello-world/ui/\n");
					htmlResponse.append("Greeting API via Gateway         https://localhost:8210/hello-world/api/greeting/get-greeting\n");
					htmlResponse.append("Clock API via Gateway            https://localhost:8210/hello-world/api/clock/get-time\n");
					htmlResponse.append("Calendar API via Gateway         https://localhost:8210/hello-world/api/calendar/get-date\n\n");
	
					htmlResponse.append("UI no Gateway                    https://localhost:8211/hello-world/ui/\n");
					htmlResponse.append("Greeting API no Gateway          https://localhost:8212/hello-world/api/greeting/get-greeting\n");
					htmlResponse.append("Clock API no Gateway             https://localhost:8213/hello-world/api/clock/get-time\n");
					htmlResponse.append("Calendar API no Gateway          https://localhost:8214/hello-world/api/calendar/get-date\n");
				htmlResponse.append("</pre>");
			htmlResponse.append("<div/>");
		return htmlResponse.toString();
	}

}