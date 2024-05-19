package com.zonesoft.reference.utils.client_builder;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

public class ClientBuilder{
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientBuilder.class);

	public WebClient build(IClientBuilderConfigs configs) {

		WebClient webClientInstance = null;
		HttpClient httpClient = null;
		httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2_000) // millis
				.doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(2)) // seconds
						.addHandlerLast(new WriteTimeoutHandler(2))); // seconds

		webClientInstance = WebClient.builder()
				.baseUrl(configs.getProtocol() + "://" + configs.getDomain() + ":" + configs.getPort())
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.defaultCookie("client-name", configs.getClientName())
				.defaultCookie("client-type", configs.getClientType()).filter(logRequest()).filter(logResponse())
				.build();
		return webClientInstance;
	}

	private ExchangeFilterFunction logRequest() {
		return (clientRequest, next) -> {
			LOGGER.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
			LOGGER.debug("--- Http Headers: ---");
			clientRequest.headers().forEach(this::logHeader);
			LOGGER.debug("--- Http Cookies: ---");
			clientRequest.cookies().forEach(this::logHeader);
			return next.exchange(clientRequest);
		};
	}

	private ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			LOGGER.debug("Response-Code: {}", clientResponse.statusCode());
			clientResponse.headers().asHttpHeaders()
					.forEach((name, values) -> values.forEach(value -> LOGGER.debug("{}={}", name, value)));
			return Mono.just(clientResponse);
		});
	}

	private void logHeader(String name, List<String> values) {
		values.forEach(value -> LOGGER.debug("{}={}", name, value));
	}
}


