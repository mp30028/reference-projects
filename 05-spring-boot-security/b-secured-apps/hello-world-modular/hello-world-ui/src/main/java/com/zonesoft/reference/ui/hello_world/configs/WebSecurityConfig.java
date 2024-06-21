package com.zonesoft.reference.ui.hello_world.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/home").permitAll()
				.requestMatchers(HttpMethod.GET,"/show-user-greeting").hasRole("USER")
				.requestMatchers(HttpMethod.GET,"/show-admin-greeting").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.formLogin();
		http
			.logout(signOut -> signOut
				.invalidateHttpSession(true)
			    .clearAuthentication(true)
			    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			    .logoutSuccessUrl("/signed-out")
			    .permitAll());
		return http.build();
	}
	
	  @Bean
	  public UserDetailsService userDetailsService() {
	    InMemoryUserDetailsManager userDetailsManager = 
	      new InMemoryUserDetailsManager();
	    userDetailsManager.createUser( 
	      User.withDefaultPasswordEncoder() 
	        .username("user") 
	        .password("password") 
	        .roles("USER") 
	        .build());
	    userDetailsManager.createUser( 
	      User.withDefaultPasswordEncoder() 
	        .username("admin") 
	        .password("password") 
	        .roles("ADMIN")
	        .build());
	    return userDetailsManager;
	  }
	
}
