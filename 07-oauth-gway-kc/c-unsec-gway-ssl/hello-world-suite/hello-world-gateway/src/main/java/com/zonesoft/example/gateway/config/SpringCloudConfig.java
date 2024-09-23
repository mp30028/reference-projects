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
        		.route("hello-world-ui", r -> r.path("/hello-world/ui/**").uri("https://localhost:8211"))
                .route("hello-world-api-greeting", r -> r.path("/hello-world/api/greeting/**").uri("https://localhost:8212"))
                .route("hello-world-api-calendar", r -> r.path("/hello-world/api/calendar/**").uri("https://localhost:8214"))
                .route("hello-world-api-clock", r -> r.path("/hello-world/api/clock/**").uri("https://localhost:8213"))
                .route("default-route", r -> r.path("/hello-world/ui/**").uri("https://localhost:8211"))
                .build();
    }
}