package ru.khomyakov.gateway.configs;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RouteConfig {
    private Filter filter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("client", p -> p
                        .path("/api/client/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))
                .build();

    }

}
