package com.zonesoft.reference.services.greeting.configs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

	public static final String ROLE_PREFIX = "ROLE_";
	private static final String RESOURCE_ACCESS = "resource_access";
	private static final String REALM_ACCESS = "realm_access";
	private static final String ROLES = "roles";
	private static final String OAUTH2_CLIENT_ID = "azp";
//	@Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
//	private String oauth2ClientId;
	
    @Bean
    @Order(value = 1)
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/", "/hello-world/api/greeting/home").permitAll()
//                        .requestMatchers("/hello-world/api/greeting/get-greeting").hasAnyAuthority("HELLO_WORLD_USER","HELLO_WORLD_ADMIN")
//                        .requestMatchers("/hello-world/api/greeting/info").hasAuthority("HELLO_WORLD_ADMIN")
                        .anyRequest().authenticated()
                );
        http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
//		http.oauth2ResourceServer(oauth2 -> oauth2
//				.jwt(customizer -> customizer
//					.jwtAuthenticationConverter(jwtAuthenticationConverter())
//				)
//			);        
        return http.build();
    }	
	
//    @Bean
//    @Order(value = 2)
//    public SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {
////        http
////                .csrf(AbstractHttpConfigurer::disable)
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .authorizeHttpRequests(authz -> authz
////                        .requestMatchers("/", "/hello-world/api/greeting/home").permitAll()
////                        .requestMatchers("/hello-world/api/greeting/get-greeting").hasAnyAuthority("HELLO_WORLD_USER","HELLO_WORLD_ADMIN")
////                        .requestMatchers("/hello-world/api/greeting/info").hasAuthority("HELLO_WORLD_ADMIN")
////                        .anyRequest().authenticated()
////                );
//		http.oauth2ResourceServer(oauth2 -> oauth2
//				.jwt(customizer -> customizer
//					.jwtAuthenticationConverter(jwtAuthenticationConverter())
//				)
//			);        
//        return http.build();
//    }
    
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        
        return jwtAuthenticationConverter;
    }    

    public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    	@SuppressWarnings("unchecked")
    	@Override
    	public Collection<GrantedAuthority> convert(final Jwt jwt) {
    		return (Collection<GrantedAuthority>) extractRoles(jwt);
    	}
    }    

    
	private Collection<? extends GrantedAuthority> extractRoles(Jwt idToken) {
		LOGGER.debug("extractRoles from  idToken invoked");
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		Map<String, Object> claims = idToken.getClaims();
		debugLogClaims("extractAuthorities(idToken)", claims);
		extractKeycloakClientRoles(claims, grantedAuthorities);
		extractKeycloakRealmRoles(claims, grantedAuthorities);
		return grantedAuthorities;		
	}
	
	@SuppressWarnings("unchecked")
	private void extractKeycloakClientRoles(Map<String, Object> claims, Collection<GrantedAuthority> grantedAuthorities) {
        Map<String, Object> clientRoleClaims = (Map<String, Object>) claims.get(RESOURCE_ACCESS);
        String oauth2ClientId = (String) claims.get(OAUTH2_CLIENT_ID);
        LOGGER.debug("FROM extractKeycloakClientRoles: function invoked with oauth2ClientId={}",oauth2ClientId);
        debugLogClaims("extractKeycloakClientRoles", claims);
        Collection<String> clientRoles = null;
        if (Objects.nonNull(oauth2ClientId) && Objects.nonNull(clientRoleClaims)) {
        	LOGGER.debug("FROM extractKeycloakClientRoles: will attempt extracting client roles");
        	clientRoles = (Collection<String>) ((Map<String, Object>) clientRoleClaims.get(oauth2ClientId)).get(ROLES);
        	if(Objects.nonNull(clientRoles)) {
        		LOGGER.debug("FROM extractKeycloakClientRoles: client roles found in claims, starting extraction");
        		Set<SimpleGrantedAuthority> authorities = clientRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toSet());
        		grantedAuthorities.addAll(authorities);
        	}else{
        		LOGGER.debug("FROM extractKeycloakClientRoles: no client roles were found in the token");
        	}
        }
	}
	
	@SuppressWarnings("unchecked")
	private void extractKeycloakRealmRoles(Map<String, Object> claims, Collection<GrantedAuthority> grantedAuthorities) {
        Map<String, Object> realmAccess = (Map<String, Object>) claims.get(REALM_ACCESS);
        Collection<String> realmRoles;
        if (Objects.nonNull(realmAccess)) {
        	realmRoles = (Collection<String>) ((Map<String, Object>) realmAccess).get(ROLES);
        	if(Objects.nonNull(realmRoles)) {        	
        		Set<SimpleGrantedAuthority> authorities =realmRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toSet());
        		grantedAuthorities.addAll(authorities);
        	}
        }
	}	
		
	private void debugLogClaims(String label, Map<String, Object> claims) {
		if (LOGGER.isDebugEnabled()) {
			for(String claim : claims.keySet()) {
				LOGGER.debug("FROM: {}: {} = {}",label, claim, claims.get(claim));
			}
		}
	}    
    
}