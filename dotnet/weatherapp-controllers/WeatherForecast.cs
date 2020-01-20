using System;
using System.Text.Json.Serialization;

namespace weatherapp_controllers
{
    public class WeatherForecast
    {
        [JsonPropertyName("weather")]
        public string Weather { get; set; }
    }
}
