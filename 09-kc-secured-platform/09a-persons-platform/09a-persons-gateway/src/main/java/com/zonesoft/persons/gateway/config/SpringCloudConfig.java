package com.zonesoft.persons.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class SpringCloudConfig {
	
	@Value("${zonesoft.gateway.route.api-person-path}")
	private String apiPersonPath;	

	@Value("${zonesoft.gateway.route.api-person-uri}")
	private String apiPersonUri;	

	@Value("${zonesoft.gateway.route.api-configs-path}")
	private String apiConfigsPath;	

	@Value("${zonesoft.gateway.route.api-configs-uri}")
	private String apiConfigsUri;	
	
	
	@Value("${zonesoft.gateway.route.default-path}")
	private String defaultPath;	

	@Value("${zonesoft.gateway.route.default-uri}")
	private String defaultUri;	
	
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("api-person", r -> r.path(apiPersonPath).filters(f -> f.tokenRelay()).uri(apiPersonUri))
        		.route("api-configs", r -> r.path(apiConfigsPath).filters(f -> f.tokenRelay()).uri(apiConfigsUri))
        		.route("default", r -> r.path(defaultPath).filters(f -> f.tokenRelay()).uri(defaultUri))
                .build();
    } 
}