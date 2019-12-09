using System;
using System.Text.Json.Serialization;

namespace weatherapp_route_to_code
{
    public class WeatherForecast
    {
        [JsonPropertyName("location")]
        public string Location { get; set; }

        [JsonPropertyName("forecast")]
        public string Forecast { get; set; }
    }
}
