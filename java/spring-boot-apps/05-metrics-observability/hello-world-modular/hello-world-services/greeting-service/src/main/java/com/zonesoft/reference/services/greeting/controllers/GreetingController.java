package com.zonesoft.reference.services.greeting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.zonesoft.reference.services.greeting.client_configs.CalendarClientConfigs;
import com.zonesoft.reference.services.greeting.client_configs.ClockClientConfigs;
import com.zonesoft.reference.utils.client_builder.ClientBuilder;
import com.zonesoft.reference.utils.client_builder.IClientBuilderConfigs;


@RestController
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
	
	@GetMapping(value={"/greeting"})
	@ResponseBody
	public ResponseEntity<String> greeting() {
		String resultDate = invokeService(calendarClientConfigs);
		String resultTime = invokeService(clockClientConfigs);		
		return ResponseEntity.ok("Hello from greeting service on " + resultDate + " at GMT " + resultTime);
	}

	private String invokeService(IClientBuilderConfigs configs) {
		ClientBuilder builder = new ClientBuilder();
		String uriPath = configs.getPath();
		WebClient webClient = builder.build(configs);
//		webClient.mutate().baseUrl(uriPath);
		String result = webClient
				.get()
				.uri(uriBuilder -> {
					return uriBuilder.path(uriPath).build();
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
}