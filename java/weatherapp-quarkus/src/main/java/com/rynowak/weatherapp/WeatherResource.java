package com.rynowak.weatherapp;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;


@Path("/")
public class WeatherResource {

	@Inject
    @RestClient
    ForecastService forecastService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<WeatherReport> getWeather() {  	
    		return forecastService
    				.getForecast()
    				.thenApply(forecast -> new WeatherReport("Seattle", forecast.getWeather()));
    }
}