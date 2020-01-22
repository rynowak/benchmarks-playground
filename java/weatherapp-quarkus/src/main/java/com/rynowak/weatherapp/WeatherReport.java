package com.rynowak.weatherapp;

public class WeatherReport {
	private String forecast;
	private String location;
	
	public WeatherReport() {
	}
	
	public WeatherReport(String location, String forecast) {
		this.location = location;
		this.forecast = forecast;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getForecast() {
		return forecast;
	}
	public void setForecast(String forecast) {
		this.forecast = forecast;
	}
}
