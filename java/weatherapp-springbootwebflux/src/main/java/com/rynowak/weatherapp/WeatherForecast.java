package com.rynowak.weatherapp;

public class WeatherForecast {
    private String weather;

    public WeatherForecast(){
    }

    public WeatherForecast(String weather) {
        this.weather = weather;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}