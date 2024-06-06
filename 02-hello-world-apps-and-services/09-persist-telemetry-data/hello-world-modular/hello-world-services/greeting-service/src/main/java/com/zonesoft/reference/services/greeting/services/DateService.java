package com.zonesoft.reference.services.greeting.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.zonesoft.reference.services.greeting.client.configs.CalendarClientConfigs;
import io.opentelemetry.api.OpenTelemetry;

@Service
public class DateService extends AbstractInstrumentedService {

	public DateService(OpenTelemetry openTelemetry, Builder webClientBuilder, CalendarClientConfigs configs) {
		super(openTelemetry, webClientBuilder, configs);
	}

}
