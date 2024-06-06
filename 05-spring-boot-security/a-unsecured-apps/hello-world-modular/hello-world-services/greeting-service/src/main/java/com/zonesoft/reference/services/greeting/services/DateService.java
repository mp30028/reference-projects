package com.zonesoft.reference.services.greeting.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.zonesoft.reference.services.greeting.client.configs.CalendarClientConfigs;

@Service
public class DateService extends AbstractInstrumentedService {

	public DateService(Builder webClientBuilder, CalendarClientConfigs configs) {
		super(webClientBuilder, configs);
	}

}
