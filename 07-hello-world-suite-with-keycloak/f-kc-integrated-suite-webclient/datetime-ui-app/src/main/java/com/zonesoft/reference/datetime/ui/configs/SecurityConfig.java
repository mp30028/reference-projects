package com.zonesoft.reference.datetime.ui.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
      http.csrf(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests(authz -> authz
				.requestMatchers("/", "/home", "/get-token-api", "/token-request-page", "/get-token-ui", "/favicon.ico").permitAll()
				.anyRequest().authenticated());
		http.oauth2Client(Customizer.withDefaults());
		http.oauth2Login(Customizer.withDefaults());
		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));		
		return http.build();
	}
}


