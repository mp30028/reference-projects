package com.zonesoft.reference.ui.hello_world.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

import com.zonesoft.reference.ui.hello_world.clients.GreetingServiceClientConfigs;
import com.zonesoft.reference.utils.client_builder.ClientBuilder;

@Controller
public class FrontController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);
	private final GreetingServiceClientConfigs builderConfigs;

	public FrontController(GreetingServiceClientConfigs builderConfigs) {
		super();
		this.builderConfigs = builderConfigs;
	}

	@GetMapping(value = { "show-greeting" })
	@ResponseBody
	public String greeting() {
		LOGGER.debug("Request to show greting received");
		String serviceMessage = getMessage();
		LOGGER.debug("Greeting Service invoked and returned the following message:-\n\t\t{}", serviceMessage);
		StringBuilder htmlResponse = new StringBuilder();
		htmlResponse.append("<h3>Here is the message from greeting service</h3>");
		htmlResponse.append("<h4>");
		htmlResponse.append(serviceMessage);
		htmlResponse.append("</h4>");
		htmlResponse.append("<br/><br/>");
		return htmlResponse.toString();
	}

	private String getMessage() {
		ClientBuilder builder = new ClientBuilder();
		String uriPath = this.builderConfigs.getPath();
		WebClient webClient = builder.build(this.builderConfigs);
		return webClient.get().uri(uriBuilder -> {
			return uriBuilder.path(uriPath).build();
		}).exchangeToMono(r -> {
			if (r.statusCode().equals(HttpStatus.OK)) {
				return r.bodyToMono(String.class);
			} else {
				throw new RuntimeException("Error invoking Greeting Service");
			}
		}).block();
	}
}