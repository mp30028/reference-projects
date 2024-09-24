package com.zonesoft.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain configure(ServerHttpSecurity http) throws Exception {
      http.csrf(ServerHttpSecurity.CsrfSpec::disable);
		http.authorizeExchange(authz -> authz.anyExchange().authenticated());
		http.oauth2Client(Customizer.withDefaults());
		http.oauth2Login(Customizer.withDefaults());
		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));		
		return http.build();
	}

}
