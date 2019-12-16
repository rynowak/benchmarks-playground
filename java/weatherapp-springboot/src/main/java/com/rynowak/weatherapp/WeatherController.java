package com.rynowak.weatherapp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WeatherController {

    @RequestMapping("/")
    public WeatherForecast weather() {
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.getForEntity("http://localhost:5000/forecast", String.class);
        return new WeatherForecast("Seattle", response.getBody());
    }

    @RequestMapping(path = "/forecast", produces = "text/plain")
    public String forecast() {
        return "Cloudy";
    }
}