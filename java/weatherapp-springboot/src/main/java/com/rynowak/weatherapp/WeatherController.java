package com.rynowak.weatherapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WeatherController {

    private RestTemplate restTemplate;
    private String uri;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        String uri = System.getenv("FORECAST_SERVICE_URI");
        if (uri == null)
        {
            uri = "http://localhost:5002";
        }
        if (uri.endsWith("/"))
        {
            uri.substring(0, uri.length() - 1);
        }

        this.uri = uri + "/forecast";
    }

    @RequestMapping("/")
    public WeatherReport weather() {
        ResponseEntity<WeatherForecast> response = restTemplate.getForEntity(uri, WeatherForecast.class);
        return new WeatherReport("Seattle", response.getBody().getWeather());
    }
}