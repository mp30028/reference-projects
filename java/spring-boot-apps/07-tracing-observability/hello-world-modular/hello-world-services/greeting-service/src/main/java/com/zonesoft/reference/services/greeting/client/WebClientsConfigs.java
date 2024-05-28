package com.zonesoft.reference.services.greeting.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientsConfigs {
	
	@Bean
	public WebClients webClients() {
		return new WebClients();
	}
}
