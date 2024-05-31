package com.zonesoft.reference.services.greeting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import com.zonesoft.reference.services.greeting.client.WebClients;
import com.zonesoft.reference.services.greeting.client.configs.CalendarClientConfigs;
import com.zonesoft.reference.services.greeting.client.configs.ClockClientConfigs;
import com.zonesoft.reference.utils.webclient.builder.IClientConfigs;


@RestController
public class GreetingController {

	private final IClientConfigs calendarClientConfigs;
	private final IClientConfigs clockClientConfigs;
	private final WebClients webClients;

	
	@Autowired
	public GreetingController(WebClients webClients, CalendarClientConfigs calendarClientConfigs, ClockClientConfigs clockClientConfigs) {
		super();
		this.webClients = webClients;
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
}