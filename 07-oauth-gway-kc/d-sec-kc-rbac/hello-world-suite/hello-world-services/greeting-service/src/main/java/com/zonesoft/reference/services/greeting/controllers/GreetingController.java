package com.zonesoft.reference.services.greeting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.zonesoft.reference.services.greeting.client_configs.CalendarClientConfigs;
import com.zonesoft.reference.services.greeting.client_configs.ClockClientConfigs;
import com.zonesoft.reference.utils.client_builder.IClientBuilderConfigs;

import reactor.core.publisher.Mono;


@RestController
@RequestMapping(value={"/hello-world/api/greeting"})
public class GreetingController {

	private final CalendarClientConfigs calendarClientConfigs;
	private final ClockClientConfigs clockClientConfigs;
	private final WebClient webClient;
	
	@Autowired
	public GreetingController(
			CalendarClientConfigs calendarClientConfigs, 
			ClockClientConfigs clockClientConfigs,
			WebClient webClient) {
		super();
		this.calendarClientConfigs = calendarClientConfigs;
		this.clockClientConfigs = clockClientConfigs;
		this.webClient = webClient;
	}	
	
	@GetMapping(value={"/get-greeting"})
	@ResponseBody
	public Mono<ResponseEntity<String>> greeting() {
		Mono<String> resultDateMono = invokeService(calendarClientConfigs);
		Mono<String> resultTimeMono = invokeService(clockClientConfigs);
		return 
				Mono
					.zip(resultDateMono,resultTimeMono)
					.map(zippedMono ->ResponseEntity.ok("Hello from greeting service on " + zippedMono.getT1() + " at GMT " + zippedMono.getT2()));
//		return Mono.just(ResponseEntity.ok("****************************** TEMPORARY-PLACEHOLDER-VALUE *******************************"));
	}

	private Mono<String> invokeService(IClientBuilderConfigs configs) {
//		ClientBuilder builder = new ClientBuilder();

		//WebClient webClient = builder.build(configs);
//		webClient.mutate().baseUrl(uriPath);
		String baseUrl =  configs.getProtocol() + "://" + configs.getDomain() + ":" + configs.getPort();
		String greetingPath = configs.getPath();		
		Mono<String> result = webClient
				.get()
				.uri(baseUrl + greetingPath)
//				.uri(uriBuilder -> {
//					return uriBuilder.path(uriPath).build();
//				})
//				.attributes(oauth2AuthorizedClient(authorizedClient))
				.retrieve()
				.bodyToMono(String.class);
		return result;
	}
}