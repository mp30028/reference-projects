package com.zonesoft.persons.ui.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import com.zonesoft.persons.utils.KeycloakRolesConverter;

@RestController
@RequestMapping("/api/configs")
public class ApiConfigsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiConfigsController.class);
	
	@Value(value = "${zonesoft.persons.api.protocol}")
	private String personsApiProtocol;	
	
	@Value(value = "${zonesoft.persons.api.host}")
	private String personsApiHost;	

	@Value(value = "${zonesoft.persons.api.port}")
	private String personsApiPort;	

	@Value(value = "${zonesoft.persons.api.path}")
	private String personsApiPath;	
	
	@GetMapping(produces="application/json" )
	@ResponseBody
	public Mono<ResponseEntity<ApiConfigs>> getConfigs(){
		ApiConfigs personsApiConfigs = new ApiConfigs();
		personsApiConfigs.protocol = this.personsApiProtocol;
		personsApiConfigs.host = this.personsApiHost;
		personsApiConfigs.port = Integer.parseInt(this.personsApiPort);
		personsApiConfigs.path = this.personsApiPath;
		Jwt jwtAuthToken = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		personsApiConfigs.authInfo = extractAuthInfoFromToken(jwtAuthToken);		
		LOGGER.debug("personsApiConfigs={}",personsApiConfigs);
		return Mono.just(ResponseEntity.ok().body(personsApiConfigs));
	}
	
	
	private Map<String,Object> extractAuthInfoFromToken(Jwt jwtAuthToken) {
		LOGGER.debug("extractAuthInfoFromToken from  jwtAuthToken invoked. jwtAuthToken={}", jwtAuthToken);		
		Map<String, Object> claims = jwtAuthToken.getClaims();
		debugLogClaims(claims);
		Map<String,Object> info = new HashMap<>();
		info.put("issuer", claims.get("iss"));
		info.put("id", claims.get("sub"));
		info.put("username", claims.get("preferred_username"));
		info.put("fullname", claims.get("name"));
		info.put("email", claims.get("email"));
		Instant instantAuthTime = Instant.ofEpochSecond((long) claims.get("auth_time"));
		info.put("authenticationTime", instantAuthTime.toString());
		Instant instantIat = (Instant)(claims.get("iat"));
		info.put("tokenIssuedAt", instantIat.toString());
		info.put("roles", extractRolesFromToken(jwtAuthToken));
		return info;
	}

	private List<String> extractRolesFromToken(Jwt jwtAuthToken){
		KeycloakRolesConverter converter = new KeycloakRolesConverter();
		ArrayList<GrantedAuthority> authorities = (ArrayList<GrantedAuthority>) converter.convert(jwtAuthToken);		
		return authorities.stream().map(item -> item.getAuthority()).toList();
	}
	
	
	private void debugLogClaims(Map<String, Object> claims) {
		if (LOGGER.isDebugEnabled()) {
			for(String claim : claims.keySet()) {
				LOGGER.debug("{} = {}",claim, claims.get(claim));
			}
		}
	}	
	
	class ApiConfigs{
		public String protocol;
		public String host;
		public int port;
		public String path;
		public Map<String, Object> authInfo;
	}

}
