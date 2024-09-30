package com.zonesoft.reference.services.greeting.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import com.zonesoft.reference.utils.KeycloakRolesConverter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
    @Bean
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authz -> authz
						.requestMatchers("/", "/hello-world/api/greeting/home").permitAll()
						.requestMatchers("/hello-world/api/greeting/get-greeting")
						.hasAnyAuthority("HELLO_WORLD_USER", "HELLO_WORLD_ADMIN")
						.requestMatchers("/hello-world/api/greeting/info").hasAuthority("HELLO_WORLD_ADMIN")
						.anyRequest().authenticated());
		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(customizer -> customizer.jwtAuthenticationConverter(jwtAuthenticationConverter())));      
        return http.build();
    }	
	   
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRolesConverter());        
        return jwtAuthenticationConverter;
    }    

}