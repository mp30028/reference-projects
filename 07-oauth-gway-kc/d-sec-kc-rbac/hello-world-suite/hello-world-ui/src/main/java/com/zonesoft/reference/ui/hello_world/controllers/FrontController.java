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
import reactor.core.publisher.Mono;

@Controller
@RequestMapping(value={"/hello-world/ui"})
public class FrontController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);
	private final GreetingServiceClientConfigs builderConfigs;
	private final WebClient webClient;

	public FrontController(GreetingServiceClientConfigs builderConfigs,WebClient webClient) {
		super();
		this.builderConfigs = builderConfigs;
		this.webClient = webClient;
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
		String baseUrl =  builderConfigs.getProtocol() + "://" + builderConfigs.getDomain() + ":" + builderConfigs.getPort();
		String greetingPath = builderConfigs.getPath();
		Mono<String> result =  this.webClient
				.get()
				.uri(baseUrl + greetingPath)
				.retrieve()
				.bodyToMono(String.class);
		return result;
	}
	
}