package com.zonesoft.reference.ui.hello_world.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.zonesoft.reference.ui.hello_world.configs.clients.GreetingClientConfigs;
import com.zonesoft.reference.utils.webclient.helper.WebClientHelper;

@Configuration
public class WebClientConfig {
	private static final WebClientHelper webClientHelper = new WebClientHelper();
	
  @Bean
  WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager, WebClient.Builder webClientBuilder, GreetingClientConfigs configs) {	  
    ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
		return webClientHelper.buildClient(webClientBuilder, configs, oauth2Client);	  
  }
}