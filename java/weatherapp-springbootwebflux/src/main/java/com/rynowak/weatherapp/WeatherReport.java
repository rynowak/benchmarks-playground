package com.rynowak.weatherapp;

public class WeatherReport {
    private String location;
    private String forecast;

    public WeatherReport(String location, String forecast) {
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