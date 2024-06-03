package com.zonesoft.reference.services.greeting.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import com.zonesoft.reference.utils.webclient.helper.IClientConfigs;
import com.zonesoft.reference.utils.webclient.helper.WebClientHelper;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.incubator.trace.ExtendedTracer;
import io.opentelemetry.api.trace.Span;

public abstract class AbstractInstrumentedService {
	private static final String INSTRUMENTATION_NAME = AbstractInstrumentedService.class.getName();
	private static final WebClientHelper webClientHelper = new WebClientHelper();
	private final ExtendedTracer tracer;
	private final IClientConfigs configs;
	private final WebClient webClient;	
	
	public AbstractInstrumentedService(OpenTelemetry openTelemetry, WebClient.Builder webClientBuilder, IClientConfigs configs) {
		super();
		tracer = ExtendedTracer.create(openTelemetry.getTracer(INSTRUMENTATION_NAME));
		this.webClient = webClientHelper.buildClient(webClientBuilder, configs);
		this.configs = configs;
	}
	
	public String invoke() {
//	      try {
//			return 
//				Baggage.current()
//				//.put("foo", "bar")
//				//.build()
//				.storeInContext(Context.current())
//				
//				.wrap(() -> tracer.spanBuilder("span with baggage").startAndCall(this::doInvoke))
//				.call();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
		
		tracer.spanBuilder("AbstractInstrumentedService-invoke").startAndCall(this::doInvoke);
		return doInvoke();	
	}
	
	private String doInvoke() {
		Span.current().setAttribute("dummy-attribute", "dummy-attribute-value");
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
		Span.current().setAttribute("invocation-result", result);
		return result;
	}
}
