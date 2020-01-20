package com.rynowak.weatherapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class WeatherHandler {

    private WeatherClient weatherClient;

    @Autowired
    public void setWeatherClient(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public Mono<ServerResponse> weather(ServerRequest request) {
        return weatherClient.getForecast()
                .flatMap(forecast -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(new WeatherReport("Seattle", forecast.getWeather()))));
    }
}