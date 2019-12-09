package com.rynowak.weatherapp;

public class WeatherForecast {
    private String location;
    private String forecast;

    public WeatherForecast(String location, String forecast) {
        this.location = location;
        this.forecast = forecast;
    }

    public String getLocation() {
        return location;
    }

    public String getForecast() {
        return forecast;
    }
}