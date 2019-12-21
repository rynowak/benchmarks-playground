package com.rynowak.weatherapp;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class WeatherClient {
    private WebClient client = WebClient.create("http://localhost:5000");

    private Mono<ClientResponse> result = client.get().uri("/forecast").accept(MediaType.TEXT_PLAIN).exchange();

    public Mono<String> getForecast() {
        return result.flatMap(res -> res.bodyToMono(String.class));
    }
}