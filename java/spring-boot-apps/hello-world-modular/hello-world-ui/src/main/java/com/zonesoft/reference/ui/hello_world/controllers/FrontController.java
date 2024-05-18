package com.zonesoft.reference.ui.hello_world.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

import com.zonesoft.reference.ui.hello_world.clients.GreetingServiceClient;
import com.zonesoft.reference.ui.hello_world.clients.builder.ClientBuilder;

@Controller
public class FrontController {

	private final ClientBuilder<GreetingServiceClient> builder;
	
	public FrontController(ClientBuilder<GreetingServiceClient> builder) {
		super();
		this.builder = builder;
	}
	
	@GetMapping(value={"show-greeting"})
	@ResponseBody
	public String greeting( ) {
			String serviceMessage = getMessage();
			StringBuilder htmlResponse = new StringBuilder();
			htmlResponse.append("<h3>Here is the message from greeting service</h3>");
			htmlResponse.append("<h4>");
				htmlResponse.append(serviceMessage);
			htmlResponse.append("</h4>");
			htmlResponse.append("<br/><br/>");
			return htmlResponse.toString();

	}
	
	private String getMessage() {
		String uriPath = this.builder.getConfigs().getPath();
		WebClient webClient = builder.build();
		return webClient
				.get()
				.uri(uriBuilder -> {
					return uriBuilder.path(uriPath).build();
				})
				.exchangeToMono(r -> {
					if (r.statusCode().equals(HttpStatus.OK)) {
						return r.bodyToMono(String.class);
					} else {
						throw new RuntimeException("Error invoking Greeting Service");
					}
				})
				.block();
	}

}