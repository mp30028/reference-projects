package com.zonesoft.reference.ui.hello_world.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests((requests) -> requests
			.requestMatchers("/", "/home").permitAll()
//			.requestMatchers("/show-user-greeting").authenticated()
//			.requestMatchers(HttpMethod.GET,"/show-admin-greeting").hasRole("USER")
//			.requestMatchers(HttpMethod.GET,"/show-admin-greeting").hasRole("ADMIN")
			
			.anyRequest().authenticated()
		);
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
       http.oauth2Login(oauth2Login -> oauth2Login
       			.loginPage("/oauth2/authorization/hello-world-ui-oidc"))
       			.oauth2Client(withDefaults());
		
//       http.formLogin((form) -> form
//					.loginPage("/login")
//					.permitAll()
//			);
//       http.logout(signOut -> signOut
//				.invalidateHttpSession(true)
//			    .clearAuthentication(true)
//			    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//			    .logoutSuccessUrl("/signed-out")
//			    .permitAll());
       http.csrf((csrf) -> csrf
        		.disable()
        );
       
		return http.build();
	}
}
