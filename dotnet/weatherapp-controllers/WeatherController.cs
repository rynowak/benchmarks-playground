using Microsoft.AspNetCore.Mvc;

namespace weatherapp_controllers
{
    [ApiController]
    public class WeatherController : ControllerBase
    {
        [HttpGet("/")]
        public ActionResult<WeatherForecast> GetWeather()
        {
            return new WeatherForecast() { Location = "Seattle", Forecast = "Cloudy", };
        }
    }
}