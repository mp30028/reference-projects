package com.zonesoft.reference.services.clock.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests
				.anyRequest().authenticated()
			);
	    http.httpBasic();
        http.csrf((csrf) -> csrf.disable());
		return http.build();
	}
	
	  @Bean
	  public UserDetailsService userDetailsService() {
	    InMemoryUserDetailsManager userDetailsManager = 
	      new InMemoryUserDetailsManager();
	    userDetailsManager.createUser( 
	      User.withDefaultPasswordEncoder() 
	        .username("time-service-user") 
	        .password("password") 
	        .roles("USER") 
	        .build());
	    return userDetailsManager;
	  }
	
}
