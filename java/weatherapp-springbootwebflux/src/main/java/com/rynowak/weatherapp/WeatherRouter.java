package com.rynowak.weatherapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class WeatherRouter {

    @Bean
    public RouterFunction<ServerResponse> route(WeatherHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        handler::weather)
                .and(RouterFunctions.route(
                        RequestPredicates.GET("/forecast").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                        handler::forecast));
    }
}