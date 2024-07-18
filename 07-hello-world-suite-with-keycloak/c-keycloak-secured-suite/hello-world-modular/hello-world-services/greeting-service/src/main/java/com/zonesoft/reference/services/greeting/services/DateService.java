package com.zonesoft.reference.services.greeting.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.zonesoft.reference.services.greeting.configs.clients.CalendarClientConfigs;

@Service
public class DateService extends AbstractInstrumentedService {

	private static final String DATE_SERVICE_UID = "date-service-user";
	private static final String DATE_SERVICE_PWD = "password";

	public DateService(Builder webClientBuilder, CalendarClientConfigs configs) {
		super(webClientBuilder, configs, DATE_SERVICE_UID, DATE_SERVICE_PWD);
	}

}
