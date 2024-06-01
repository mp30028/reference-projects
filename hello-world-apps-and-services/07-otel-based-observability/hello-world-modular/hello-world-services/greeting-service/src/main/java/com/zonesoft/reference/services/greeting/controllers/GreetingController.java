package com.zonesoft.reference.services.greeting.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import com.zonesoft.reference.services.greeting.client.configs.CalendarClientConfigs;
import com.zonesoft.reference.services.greeting.client.configs.ClockClientConfigs;
import com.zonesoft.reference.utils.webclient.helper.IClientConfigs;
import com.zonesoft.reference.utils.webclient.helper.WebClientHelper;


@RestController
public class GreetingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);
	private static final WebClientHelper webClientHelper = new WebClientHelper();
	private final IClientConfigs calendarClientConfigs;
	private final IClientConfigs clockClientConfigs;
	private final WebClient calendarClient;
	private final WebClient clockClient;

	
	@Autowired
	public GreetingController(WebClient.Builder webClientBuilder, CalendarClientConfigs calendarClientConfigs, ClockClientConfigs clockClientConfigs) {
		super();
		this.calendarClient = webClientHelper.buildClient(webClientBuilder, calendarClientConfigs);
		this.clockClient = webClientHelper.buildClient(webClientBuilder, clockClientConfigs);;
		this.calendarClientConfigs = calendarClientConfigs;
		this.clockClientConfigs = clockClientConfigs;
	}	

	@GetMapping(value={"/greeting"})
	@ResponseBody
	public ResponseEntity<String> greeting() {
		String resultDate = invokeService(calendarClient, calendarClientConfigs);
		String resultTime = invokeService(clockClient, clockClientConfigs);	
		LOGGER.debug("greeting invoked at {} on {}", resultTime, resultDate);
		return ResponseEntity.ok("Hello from greeting service on " + resultDate + " at GMT " + resultTime);
	}

	private String invokeService(WebClient webClient, IClientConfigs configs) {
		String result = webClient
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