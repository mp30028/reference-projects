package com.zonesoft.reference.ui.hello_world.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.zonesoft.reference.ui.hello_world.configs.clients.GreetingClientConfigs;
import io.opentelemetry.instrumentation.annotations.AddingSpanAttributes;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.api.trace.Span;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Service
public class GreetingClientService {
	private final GreetingClientConfigs greetingConfigs;
	private final WebClient greetingClient;
	 
	@Autowired
	public GreetingClientService(WebClient webClient, GreetingClientConfigs greetingConfigs) {
		super();
		this.greetingClient = webClient;
		this.greetingConfigs = greetingConfigs;
	}

	@WithSpan
	@AddingSpanAttributes()
	public String invoke(OAuth2AuthorizedClient authorizedClient) {
		Span.current().setAttribute("dummy-attribute", "dummy-attribute-value");
		String result = this.greetingClient
				.get()
				.uri(uriBuilder -> {
					return uriBuilder.path(greetingConfigs.getPath()).build();
				})
				.attributes(oauth2AuthorizedClient(authorizedClient))
		          .retrieve()
		          .bodyToMono(String.class)				
				.block();
		Span.current().setAttribute("invocation-result", result);
		return result;
	}	
}
