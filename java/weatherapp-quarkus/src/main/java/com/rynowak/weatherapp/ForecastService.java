package com.rynowak.weatherapp;

import java.util.concurrent.CompletionStage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey="forecast-service")
public interface ForecastService {

    @GET
    @Path("/forecast")
    @Produces("application/json")
    CompletionStage<WeatherForecast> getForecast();
}
