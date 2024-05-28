package com.zonesoft.reference.ui.hello_world.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

import com.zonesoft.reference.ui.hello_world.client.WebClients;
import com.zonesoft.reference.ui.hello_world.client.configs.GreetingClientConfigs;
import com.zonesoft.reference.utils.webclient.builder.IClientConfigs;


@Controller
public class FrontController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);
	private final IClientConfigs greetingConfigs;
	private final WebClients webClients;

	public FrontController(WebClients webClients, GreetingClientConfigs greetingConfigs) {
		super();
		this.webClients = webClients;
		this.greetingConfigs = greetingConfigs;
	}

	@GetMapping(value = { "show-greeting" })
	@ResponseBody
	public String greeting() {
		LOGGER.debug("Request to show greting received");
		String serviceMessage = invokeService(greetingConfigs);
		LOGGER.debug("Greeting Service invoked and returned the following message:-{}", serviceMessage);
		StringBuilder htmlResponse = new StringBuilder();
		htmlResponse.append("<h3>Here is the message from greeting service</h3>");
		htmlResponse.append("<h4>");
		htmlResponse.append(serviceMessage);
		htmlResponse.append("</h4>");
		htmlResponse.append("<br/><br/>");
		return htmlResponse.toString();
	}
	
	private String invokeService(IClientConfigs configs) {
		WebClient wc = webClients.getWebClient(configs);
		String result = wc
				.get()
				.uri(uriBuilder -> {
					return uriBuilder.path(configs.getPath()).build();
				})
				.exchangeToMono(r -> {
					if (r.statusCode().equals(HttpStatus.OK)) {
						return r.bodyToMono(String.class);
					} else {
						throw new RuntimeException("Error invoking Service");
					}
				})
				.block();
		return result;
	}
	
//	private String getMessage() {
//
//		return this.greetingServiceWebClient
//				.get()
//				.uri(uriBuilder -> uriBuilder.path(greetingConfigs.getPath()).build())
//				.exchangeToMono(r -> {
//					if (r.statusCode().equals(HttpStatus.OK)) {
//						return r.bodyToMono(String.class);
//					} else {
//						throw new RuntimeException("Error invoking Greeting Service");
//					}
//				})
//				.block();
//	}
}