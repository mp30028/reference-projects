package com.zonesoft.person.api.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.RequestMatchers;

import com.zonesoft.persons.utils.KeycloakRolesConverter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.GET,"/api/person/**").hasAuthority("PERSONS_PLATFORM_USER")
            .requestMatchers(HttpMethod.PUT,"/api/person/**").hasAuthority("PERSONS_PLATFORM_USER")
            .requestMatchers(HttpMethod.POST,"/api/person/**").hasAuthority("PERSONS_PLATFORM_USER")
            .requestMatchers(HttpMethod.GET,"/api/person/**").hasAuthority("PERSONS_PLATFORM_USER")
            .requestMatchers(HttpMethod.PATCH,"/api/person/**").hasAuthority("PERSONS_PLATFORM_USER")
            .requestMatchers(HttpMethod.DELETE,"/api/person/**").hasAuthority("PERSONS_PLATFORM_ADMIN")
        	.anyRequest().denyAll()
        );
//      http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
		http.oauth2ResourceServer((oauth2) -> oauth2.jwt(customizer -> customizer.jwtAuthenticationConverter(jwtAuthenticationConverter())));        
        return http.build();
    }
    
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRolesConverter());
		return jwtAuthenticationConverter;
	}
}
