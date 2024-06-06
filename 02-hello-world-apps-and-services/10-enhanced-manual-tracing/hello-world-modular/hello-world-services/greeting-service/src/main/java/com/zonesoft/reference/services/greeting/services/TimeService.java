package com.zonesoft.reference.services.greeting.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.zonesoft.reference.services.greeting.client.configs.ClockClientConfigs;

@Service
public class TimeService extends AbstractInstrumentedService {

	public TimeService(Builder webClientBuilder, ClockClientConfigs configs) {
		super(webClientBuilder, configs);
	}

}
