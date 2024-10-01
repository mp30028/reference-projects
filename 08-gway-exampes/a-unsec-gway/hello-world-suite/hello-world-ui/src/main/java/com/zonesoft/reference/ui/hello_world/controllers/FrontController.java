package com.zonesoft.reference.ui.hello_world.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

import com.zonesoft.reference.ui.hello_world.clients.GreetingServiceClientConfigs;
import com.zonesoft.reference.utils.client_builder.ClientBuilder;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping(value={"/hello-world/ui"})
public class FrontController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);
	private final GreetingServiceClientConfigs builderConfigs;

	public FrontController(GreetingServiceClientConfigs builderConfigs) {
		super();
		this.builderConfigs = builderConfigs;
	}

	@GetMapping(value = { "/show-greeting" })
	@ResponseBody
	public Mono<ResponseEntity<Mono<String>>> greeting() {
		LOGGER.debug("Request to show greting received");
		Mono<String> serviceMessageMono = getMessage();
		Mono<String> messageMono = serviceMessageMono
				.map(msg ->{
					StringBuilder htmlResponse = new StringBuilder();
					htmlResponse.append("<h3>Here is the message from greeting service</h3>");
					htmlResponse.append("<h4>");
					htmlResponse.append(msg);
					htmlResponse.append("</h4>");
					htmlResponse.append("<br/><br/>");
					htmlResponse.append("<a href=\"/hello-world/ui/\">Return to Home Page</a><br/>");							
					return htmlResponse.toString();
				}); 
		return Mono.just(ResponseEntity.ok(messageMono));
	}	

	private Mono<String> getMessage() {
		ClientBuilder builder = new ClientBuilder();
		String uriPath = this.builderConfigs.getPath();
		WebClient webClient = builder.build(this.builderConfigs);
		Mono<String> result =  webClient
				.get()
				.uri(uriBuilder -> uriBuilder.path(uriPath).build())
				.retrieve()
				.bodyToMono(String.class);
		return result;
	}
	
}