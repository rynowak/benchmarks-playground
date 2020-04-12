using System;
using Newtonsoft.Json;

namespace weatherapp_controllers_legacy
{
    public class WeatherForecast
    {
        [JsonProperty("weather")]
        public string Weather { get; set; }
    }
}
