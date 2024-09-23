package com.zonesoft.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("hello-world-ui", r -> r.path("/hello-world/ui/**").filters(f -> f.tokenRelay()).uri("https://localhost:8311"))
//        		.route("hello-world-ui", r -> r.path("/hello-world/ui/**").uri("https://localhost:8311"))
                .route("hello-world-api-greeting", r -> r.path("/hello-world/api/greeting/**").uri("https://localhost:8312"))
                .route("hello-world-api-calendar", r -> r.path("/hello-world/api/calendar/**").uri("https://localhost:8314"))
                .route("hello-world-api-clock", r -> r.path("/hello-world/api/clock/**").uri("https://localhost:8313"))
                .route("default-route", r -> r.path("/hello-world/ui/**").uri("https://localhost:8311"))
                .build();
    }
}