package com.zonesoft.reference.ui.hello_world.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.security.core.Authentication;
@Controller  
@RequestMapping(value={"/hello-world/ui"})
public class HomeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@GetMapping(value={"","/"})
	@ResponseBody
	public String greeting( ) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String scopes =  "Scopes: " + authentication.getAuthorities();
		LOGGER.debug("Request for static content received");
		LOGGER.debug("Security Context has following scopes", scopes);
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
					htmlResponse.append("This an application with a suite of supporting services all secured with Keycloak\n");
					htmlResponse.append("It consists of a basic UI whose home page you are currently viewing\n");
					htmlResponse.append("From the home page you can trigger a number of Services in a variety of ways as described \n");
					htmlResponse.append("in the sections that follow \n");
					htmlResponse.append(" \n");
				htmlResponse.append("</pre>");
			htmlResponse.append("</div>");
			
			htmlResponse.append("<hr style=\"width:700px; display: inline-block;\"/>");
			htmlResponse.append("<div style=\"width:1000px; overflow:auto\">");
				htmlResponse.append("<a href=\"/hello-world/ui/show-greeting\">Invoke get-greeting using authorised client</a>");
				htmlResponse.append("<pre style=\"white-space: pre-wrap\">");
					htmlResponse.append("The UI uses an authorised webclient to call the greeting API via the\n");
					htmlResponse.append("Gateway using the following url:-\n");
					htmlResponse.append("https://localhost:8310/hello-world/api/greeting/get-greeting\n\n");
					htmlResponse.append("The greeting API in turn uses an authorised client to call the Calendar API and the Clock API\n");
					htmlResponse.append("again via the Gateway using the following urls:-\n");
					htmlResponse.append("https://localhost:8310/hello-world/api/calendar/get-date\n");
					htmlResponse.append("https://localhost:8310/hello-world/api/clock/get-time\n\n");
					htmlResponse.append("The three api calls mentioned above are protected using role based access control (rbac)\n");
					htmlResponse.append("This means the users have to be authenticated and assigned HELLO_WORLD_USER or HELLO_WORLD_ADMIN\n");
					htmlResponse.append("Keycloak client roles\n");
				htmlResponse.append("</pre>");
			htmlResponse.append("</div>");
			
			htmlResponse.append("<hr style=\"width:700px; display: inline-block;\"/>");
			htmlResponse.append("<div style=\"width:1000px; overflow:auto\">");
				htmlResponse.append("<a href=\"https://localhost:8310/hello-world/api/greeting/get-greeting\">Invoke get-greeting not using authorised client</a>");
				htmlResponse.append("<pre style=\"white-space: pre-wrap\">");
					htmlResponse.append("Here an authorised webclient is not being used to call the greeting API\n");
					htmlResponse.append("Instead a direct call via the Gateway is made using the following url:-\n");
					htmlResponse.append("https://localhost:8310/hello-world/api/greeting/get-greeting\n\n");
					htmlResponse.append("However because the  Gateway has not been configured to relay authentication tokens to the Greeting api\n");
					htmlResponse.append("the call should fail\n");
				htmlResponse.append("</pre>");
			htmlResponse.append("</div>");			
			
			htmlResponse.append("<hr style=\"width:700px; display: inline-block;\"/>");
			htmlResponse.append("<div style=\"width:1000px; overflow:auto\">");
				htmlResponse.append("<a href=\"https://localhost:8310/hello-world/api/clock/get-time\">Invoke get-time Service not using authorised client</a>");
				htmlResponse.append("<pre style=\"white-space: pre-wrap\">");
					htmlResponse.append("Here an authorised webclient is not being used to call the clock API\n");
					htmlResponse.append("Instead a direct call via the Gateway is made using the following url:-\n");
					htmlResponse.append("https://localhost:8310/hello-world/api/clock/get-time\n\n");
					htmlResponse.append("However because the  Gateway is configured to relay authentication tokens to the Clock api, if the\n");
					htmlResponse.append("relayed token is valid and the user is assigned HELLO_WORLD_USER or HELLO_WORLD_ADMIN roles then \n");
					htmlResponse.append("the call should succeed\n");
				htmlResponse.append("</pre>");
			htmlResponse.append("</div>");				
			
			htmlResponse.append("<hr style=\"width:700px; display: inline-block;\"/>");
			htmlResponse.append("<div style=\"width:1000px; overflow:auto\">");
				htmlResponse.append("<a href=\"/logout\">Logout</a>");
				htmlResponse.append("<pre style=\"white-space: pre-wrap\">");
					htmlResponse.append("Logout will redirect to a confirmation page before completing the logout\n");
				htmlResponse.append("</pre>");
			htmlResponse.append("</div>");				
			
//			htmlResponse.append("<hr style=\"width:700px; display: inline-block;\"/>");
//			htmlResponse.append("<div style=\"width:1000px; overflow:auto\">");
//				htmlResponse.append("<a href=\"https://localhost:8310/hello-world/api/greeting/get-greeting\">Invoke Greeting Service API via the Gateway</a>");
//				htmlResponse.append("<pre style=\"white-space: pre-wrap\">");
//					htmlResponse.append("This will make a direct call from the browser to the Gateway. The Gateway will check that the user is authenticated\n");
//					htmlResponse.append("If yes then it will forward the request to the Greeting API \n\n");
//				htmlResponse.append("</pre>");
//			htmlResponse.append("</div>");			
//			
//			htmlResponse.append("<div style=\"width:1200px; overflow:auto\">");
//				htmlResponse.append("<pre style=\"white-space: pre-wrap\">");
//					htmlResponse.append("List of URLs to test with and without spring cloud gateway\n\n");
//					htmlResponse.append("UI via Gateway                   https://localhost:8310/hello-world/ui/\n");
//					htmlResponse.append("Greeting API via Gateway         https://localhost:8310/hello-world/api/greeting/get-greeting\n");
//					htmlResponse.append("Clock API via Gateway            https://localhost:8310/hello-world/api/clock/get-time\n");
//					htmlResponse.append("Calendar API via Gateway         https://localhost:8310/hello-world/api/calendar/get-date\n\n");
//	
//					htmlResponse.append("UI no Gateway                    https://localhost:8311/hello-world/ui/\n");
//					htmlResponse.append("Greeting API no Gateway          https://localhost:8312/hello-world/api/greeting/get-greeting\n");
//					htmlResponse.append("Clock API no Gateway             https://localhost:8313/hello-world/api/clock/get-time\n");
//					htmlResponse.append("Calendar API no Gateway          https://localhost:8314/hello-world/api/calendar/get-date\n");
//				htmlResponse.append("</pre>");
//			htmlResponse.append("</div>");
		return htmlResponse.toString();
	}

}