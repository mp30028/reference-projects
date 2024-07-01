package com.zonesoft.reference.ui.hello_world.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.zonesoft.reference.ui.hello_world.client.configs.GreetingClientConfigs;
import com.zonesoft.reference.utils.webclient.helper.IClientConfigs;
import com.zonesoft.reference.utils.webclient.helper.WebClientHelper;

import io.opentelemetry.instrumentation.annotations.AddingSpanAttributes;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.api.trace.Span;

@Service
public class GreetingClientService {
	private static final WebClientHelper webClientHelper = new WebClientHelper();
	private final IClientConfigs greetingConfigs;
	private final WebClient greetingClient;
	
	 
	@Autowired
	public GreetingClientService(WebClient.Builder webClientBuilder, GreetingClientConfigs greetingConfigs ) {
		super();
		this.greetingClient = webClientHelper.buildClient(webClientBuilder, greetingConfigs);
		this.greetingConfigs = greetingConfigs;
	}

	@WithSpan
	@AddingSpanAttributes()
	public String invoke() {
		Span.current().setAttribute("dummy-attribute", "dummy-attribute-value");
		String result = this.greetingClient
				.get()
				.uri(uriBuilder -> {
					return uriBuilder.path(greetingConfigs.getPath()).build();
				})
				.exchangeToMono(r -> {
					if (r.statusCode().equals(HttpStatus.OK)) {
						return r.bodyToMono(String.class);
					} else {
						throw new RuntimeException("Error invoking Service");
					}
				})
				.block();
		Span.current().setAttribute("invocation-result", result);
		return result;
	}
}
