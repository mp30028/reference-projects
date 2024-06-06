package com.zonesoft.reference.ui.hello_world.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.zonesoft.reference.ui.hello_world.client.configs.GreetingClientConfigs;
import com.zonesoft.reference.utils.webclient.helper.IClientConfigs;
import com.zonesoft.reference.utils.webclient.helper.WebClientHelper;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.incubator.trace.ExtendedTracer;
import io.opentelemetry.api.trace.Span;

@Service
public class GreetingService {
	private static final String INSTRUMENTATION_NAME = GreetingService.class.getName();
	private static final WebClientHelper webClientHelper = new WebClientHelper();
	private final ExtendedTracer tracer;
	private final IClientConfigs greetingConfigs;
	private final WebClient greetingClient;
	
	 
	@Autowired
	public GreetingService(OpenTelemetry openTelemetry, WebClient.Builder webClientBuilder, GreetingClientConfigs greetingConfigs ) {
		super();
		tracer = ExtendedTracer.create(openTelemetry.getTracer(INSTRUMENTATION_NAME));
		this.greetingClient = webClientHelper.buildClient(webClientBuilder, greetingConfigs);
		this.greetingConfigs = greetingConfigs;
	}
	
	public String invoke() {
		tracer.spanBuilder("invoke-greeting-api").startAndCall(this::doInvoke);
		return doInvoke();
	}
		
	private String doInvoke() {
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
