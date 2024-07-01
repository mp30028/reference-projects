package com.zonesoft.reference.ui.hello_world.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping(value = { "/show-user-greeting" })
	@ResponseBody
	public String greeting_1(
			@RegisteredOAuth2AuthorizedClient("hello-world-ui-authorization-code") OAuth2AuthorizedClient authorizedClient
	) {
		LOGGER.debug("Request to show USER greeting received");
		String serviceMessage = greetingClientService.invoke(authorizedClient);
		LOGGER.debug("Greeting Service invoked and returned the following message:-{}", serviceMessage);
		StringBuilder htmlResponse = new StringBuilder();
		htmlResponse.append("<h3>Here is the message from greeting service</h3>");
		htmlResponse.append("<h4>");
		htmlResponse.append(serviceMessage);
		htmlResponse.append("</h4>");
		htmlResponse.append("<br/><br/>");
		htmlResponse.append("<form method=\"GET\" action=\"/logout\">");
		htmlResponse.append("<input type=\"submit\" name=\"logoutButton\" id=\"logoutButton\" value=\"Log out\">");
		htmlResponse.append("</form>");
		return htmlResponse.toString();
	}
	
	@GetMapping(value = { "show-admin-greeting" })
	@ResponseBody
	public String greeting_2(
			@RequestParam(name="name", required = false, defaultValue = "--anonymous--")  String name,
			@RegisteredOAuth2AuthorizedClient("hello-world-ui-authorization-code") OAuth2AuthorizedClient authorizedClient
	) {
		LOGGER.debug("Request to show ADMIN greting received from someone called {}", name);
		String serviceMessage = greetingClientService.invoke(authorizedClient);
		LOGGER.debug("Greeting Service invoked and returned the following message:-{}", serviceMessage);
		StringBuilder htmlResponse = new StringBuilder();
		htmlResponse.append("<h3>Here is the message from greeting service</h3>");
		htmlResponse.append("<h4>");
		htmlResponse.append(serviceMessage);
		htmlResponse.append("<br>");
		htmlResponse.append("The requestor was identified as "); htmlResponse.append(name);
		htmlResponse.append("</h4>");
		htmlResponse.append("<br/><br/>");
		htmlResponse.append("<form method=\"GET\" action=\"/logout\">");
		htmlResponse.append("<input type=\"submit\" name=\"logoutButton\" id=\"logoutButton\" value=\"Log out\">");
		htmlResponse.append("</form>");		
		return htmlResponse.toString();
	}		
}