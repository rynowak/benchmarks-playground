using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace weatherapp_controllers_legacy
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
        public async Task<ActionResult<WeatherReport>> GetWeather()
        {
            var client = _factory.CreateClient();

            var response = await client.GetAsync("/forecast");
            response.EnsureSuccessStatusCode();

            var forecast = await response.Content.ReadAsAsync<WeatherForecast>();
            return new WeatherReport() { Location = "Seattle", Forecast = forecast.Weather, };
        }
    }
}