package com.zonesoft.reference.ui.hello_world.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientsConfigs {
	
	@Bean
	public WebClients webClients() {
		return new WebClients();
	}
}
