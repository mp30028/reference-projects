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
import com.zonesoft.reference.utils.client_builder.ClientBuilder;
import com.zonesoft.reference.utils.client_builder.IClientBuilderConfigs;

import reactor.core.publisher.Mono;


@RestController
@RequestMapping(value={"/hello-world/api/greeting"})
public class GreetingController {

	private final CalendarClientConfigs calendarClientConfigs;
	private final ClockClientConfigs clockClientConfigs;
	
	@Autowired
	public GreetingController(
			CalendarClientConfigs calendarClientConfigs, 
			ClockClientConfigs clockClientConfigs) {
		super();
		this.calendarClientConfigs = calendarClientConfigs;
		this.clockClientConfigs = clockClientConfigs;
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
	}

	private Mono<String> invokeService(IClientBuilderConfigs configs) {
		ClientBuilder builder = new ClientBuilder();
		String uriPath = configs.getPath();
		WebClient webClient = builder.build(configs);
//		webClient.mutate().baseUrl(uriPath);		
		Mono<String> result = webClient
				.get()
				.uri(uriBuilder -> {
					return uriBuilder.path(uriPath).build();
				})
				.retrieve()
				.bodyToMono(String.class);
		return result;
	}
}