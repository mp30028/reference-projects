package com.zonesoft.reference.services.greeting.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.zonesoft.reference.services.greeting.configs.clients.ClockClientConfigs;

@Service
public class TimeService extends AbstractInstrumentedService {
	
	private static final String TIME_SERVICE_UID = "time-service-user";
	private static final String TIME_SERVICE_PWD = "password";
	
	public TimeService(Builder webClientBuilder, ClockClientConfigs configs) {
		super(webClientBuilder, configs, TIME_SERVICE_UID, TIME_SERVICE_PWD);
	}

}
