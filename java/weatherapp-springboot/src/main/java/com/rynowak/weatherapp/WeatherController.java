package com.rynowak.weatherapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WeatherController {

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/")
    public WeatherReport weather() {
        ResponseEntity<WeatherForecast> response = restTemplate.getForEntity("http://localhost:5001/forecast", WeatherForecast.class);
        return new WeatherReport("Seattle", response.getBody().getWeather());
    }
}