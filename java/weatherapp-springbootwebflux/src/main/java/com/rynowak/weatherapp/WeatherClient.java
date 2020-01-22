package com.rynowak.weatherapp;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class WeatherClient {
    private WebClient client;

    public WeatherClient() {
        String uri = System.getenv("FORECAST_SERVICE_URI");
        if (uri == null)
        {
            uri = "http://localhost:5002";
        }
        this.client = WebClient.create(uri);
    }
    

    public Mono<WeatherForecast> getForecast() {
        Mono<ClientResponse> result = client.get().uri("/forecast").accept(MediaType.APPLICATION_JSON).exchange();
        return result.flatMap(res -> res.bodyToMono(WeatherForecast.class));
    }
}