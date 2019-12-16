using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace weatherapp_controllers
{
    [ApiController]
    public class WeatherController : ControllerBase
    {
        private readonly IHttpClientFactory _factory;

        public WeatherController(IHttpClientFactory factory)
        {
            _factory = factory;
        }

        [HttpGet("/")]
        public async Task<ActionResult<WeatherForecast>> GetWeather()
        {
            var client = _factory.CreateClient();
            var forecast = await client.GetStringAsync("http://localhost:5000/forecast");
            return new WeatherForecast() { Location = "Seattle", Forecast = forecast, };
        }

        [HttpGet("/forecast")]
        public ActionResult GetForecast()
        {
            return Content("Cloudy", "text/plain");
        }
    }
}