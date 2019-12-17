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
    public WeatherForecast weather() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:5000/forecast", String.class);
        return new WeatherForecast("Seattle", response.getBody());
    }

    @RequestMapping(path = "/forecast", produces = "text/plain")
    public String forecast() {
        return "Cloudy";
    }
}