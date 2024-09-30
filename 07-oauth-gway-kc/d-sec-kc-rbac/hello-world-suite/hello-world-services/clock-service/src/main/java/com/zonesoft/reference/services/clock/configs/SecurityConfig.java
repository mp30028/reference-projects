package com.zonesoft.reference.services.clock.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import com.zonesoft.reference.utils.KeycloakRolesConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/hello-world/api/clock/home").permitAll()
                        .requestMatchers("/hello-world/api/clock/get-time").hasAnyAuthority("HELLO_WORLD_USER","HELLO_WORLD_ADMIN")
                        .requestMatchers("/hello-world/api/clock/info").hasAuthority("HELLO_WORLD_ADMIN")
                        .anyRequest().authenticated()
                );
		http.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(customizer -> customizer
					.jwtAuthenticationConverter(jwtAuthenticationConverter())
				)
			);
        return http.build();
    }
    
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRolesConverter());
        
        return jwtAuthenticationConverter;
    }    
}