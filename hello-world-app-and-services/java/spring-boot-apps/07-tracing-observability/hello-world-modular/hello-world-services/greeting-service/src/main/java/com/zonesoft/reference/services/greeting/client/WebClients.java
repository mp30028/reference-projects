package com.zonesoft.reference.services.greeting.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

//import com.zonesoft.reference.services.greeting.client.configs.CalendarClientConfigs;
//import com.zonesoft.reference.services.greeting.client.configs.ClockClientConfigs;
import com.zonesoft.reference.utils.ToStringHelper;
import com.zonesoft.reference.utils.webclient.builder.IClientConfigs;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

//@Configuration
public class WebClients {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebClients.class);
//	private static final IClientConfigs calendarConfigs = new CalendarClientConfigs();
//	private static final IClientConfigs clockConfigs = new ClockClientConfigs();
//	
//	@Bean
//	public WebClient calendarServiceWebClient() {
//		return getWebClient(calendarConfigs);	
//	}
//	
//	@Bean
//	public WebClient clockServiceWebClient() {
//		return getWebClient(clockConfigs);	
//	}
	

	
	public WebClient getWebClient(IClientConfigs configs) {
		HttpClient httpClient = HttpClient
				.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2_000)
				.doOnConnected(connection -> 
					connection
						.addHandlerLast(new ReadTimeoutHandler(2 /*seconds*/)) 
						.addHandlerLast(new WriteTimeoutHandler(2 /*seconds*/))
				); 

		return WebClient.builder()
				.baseUrl(configs.getProtocol() + "://" + configs.getDomain() + ":" + configs.getPort())
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.defaultCookie("client-name", configs.getClientName())
				.defaultCookie("client-type", configs.getClientType())
				.filter(logRequest()).filter(logResponse())
				.build();		
	}	
	
	private ExchangeFilterFunction logRequest() {

		return (clientRequest, next) -> {
			ToStringHelper helper = new ToStringHelper();
			String requestLog = helper.begin()
				.wrLn("request-type", clientRequest.method())
				.wrLn("request-url", clientRequest.url())
				.wrLn("cookies", clientRequest.cookies())
				.wrLn("headers", clientRequest.headers())
			.end().build();			
			LOGGER.debug("Request: {}",requestLog);			
			return next.exchange(clientRequest);
		};
	}

	private ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			ToStringHelper helper = new ToStringHelper();
			String responseLog = helper.begin()
				.wrLn("response-status", clientResponse.statusCode())
				.wrLn("headers", clientResponse.headers().asHttpHeaders())
			.end().build();			
			LOGGER.debug("Response: {}",responseLog);	
			return Mono.just(clientResponse);
		});
	}	
	
}
