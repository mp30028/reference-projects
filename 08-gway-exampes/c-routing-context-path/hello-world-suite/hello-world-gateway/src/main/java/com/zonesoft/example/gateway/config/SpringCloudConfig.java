package com.zonesoft.example.gateway.config;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {
	
//	private static final Logger LOGGER = LoggerFactory.getLogger(SpringCloudConfig.class);
	
	
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        RouteLocator locator = builder.routes()       		
                .route("hello-world-api-greeting", r -> r.path("/hello-world/api/greeting/**").uri("https://localhost:8412"))
                .route("hello-world-api-calendar", r -> r.path("/hello-world/api/calendar/**").uri("https://localhost:8414"))
                .route("hello-world-api-clock", r -> r.path("/hello-world/api/clock/**").uri("https://localhost:8413"))
                .route("hello-world-app", r -> r.path("/hwapp/**").uri("https://localhost:8411"))
                .route("default-route", r -> r
                        .path("/**")
                        .filters(f->f.rewritePath("/(?<segment>.*)","/hwapp/${segment}"))
                        .uri("https://localhost:8411")
                      )
//              .route("default-route", r -> r.path("/**").uri("https://localhost:8411"))
//        		.route("default-route", r -> r
//        					.path("/**")
//        					.filters(f -> f.rewritePath("^\\/","/hwapp"))//rewritePath parameters 1.regex to match path   2. replacement for the match
//        					.uri("https://localhost:8411")
//        		)
                .build();
        return locator;
    }
    
    
}