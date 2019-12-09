package com.rynowak.weatherapp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @RequestMapping("/")
    public WeatherForecast greeting() {
        return new WeatherForecast("Seattle", "Cloudy");
    }
}