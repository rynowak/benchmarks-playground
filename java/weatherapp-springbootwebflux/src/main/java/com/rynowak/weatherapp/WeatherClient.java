package com.rynowak.weatherapp;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class WeatherClient {
    private WebClient client = WebClient.create("http://localhost:5002");

    private Mono<ClientResponse> result = client.get().uri("/forecast").accept(MediaType.APPLICATION_JSON).exchange();

    public Mono<WeatherForecast> getForecast() {
        return result.flatMap(res -> res.bodyToMono(WeatherForecast.class));
    }
}