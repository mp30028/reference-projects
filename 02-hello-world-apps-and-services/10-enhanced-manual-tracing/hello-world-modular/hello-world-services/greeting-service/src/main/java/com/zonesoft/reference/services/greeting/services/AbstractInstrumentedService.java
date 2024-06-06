package com.zonesoft.reference.services.greeting.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import com.zonesoft.reference.utils.webclient.helper.IClientConfigs;
import com.zonesoft.reference.utils.webclient.helper.WebClientHelper;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.AddingSpanAttributes;
import io.opentelemetry.instrumentation.annotations.WithSpan;

public abstract class AbstractInstrumentedService {
	private static final WebClientHelper webClientHelper = new WebClientHelper();
	private final IClientConfigs configs;
	private final WebClient webClient;	
	
	public AbstractInstrumentedService(WebClient.Builder webClientBuilder, IClientConfigs configs) {
		super();
		this.webClient = webClientHelper.buildClient(webClientBuilder, configs);
		this.configs = configs;
	}
	
	@WithSpan
	@AddingSpanAttributes()
	public String invoke() {
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
		Span.current().setAttribute("return-value", result);
		return result;
	}
}
