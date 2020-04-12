using System;
using Newtonsoft.Json;

namespace weatherapp_controllers_legacy
{
    public class WeatherReport
    {
        [JsonProperty("location")]
        public string Location { get; set; }

        [JsonProperty("forecast")]
        public string Forecast { get; set; }
    }
}
