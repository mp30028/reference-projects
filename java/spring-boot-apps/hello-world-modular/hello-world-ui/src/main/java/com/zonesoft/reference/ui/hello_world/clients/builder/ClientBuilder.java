package com.zonesoft.reference.ui.hello_world.clients.builder;


import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;



import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ClientBuilder<T extends IClientConfigs> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientBuilder.class);
	
	private volatile static WebClient webClientInstance = null;	
	private final T configs;
	
	@Autowired
	public  ClientBuilder(T configs) {
		this.configs = configs;
	}
	
	public IClientConfigs getConfigs() {
		return this.configs;
	}
	
    public ClientBuilder<T> reset() {
		if (Objects.nonNull(webClientInstance)) {
			synchronized (ClientBuilder.class) {
				webClientInstance = null;
			}
		}
		return this;
	}

	public WebClient build() {
		if (Objects.isNull(webClientInstance)) {
			synchronized (ClientBuilder.class) {
				if (Objects.isNull(webClientInstance)) {
					HttpClient httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2_000) // millis
							.doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(2)) // seconds
									.addHandlerLast(new WriteTimeoutHandler(2))); // seconds

					ClientBuilder.webClientInstance = WebClient.builder()
							.baseUrl(this.configs.getProtocol() + "://" + this.configs.getDomain() + ":" + this.configs.getPort())
							.clientConnector(new ReactorClientHttpConnector(httpClient))
							.defaultCookie("client-name", this.configs.getClientName()).defaultCookie("client-type", this.configs.getClientType())
							.filter(logRequest())
							.filter(logResponse()).build();
				}
			}
		}
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


