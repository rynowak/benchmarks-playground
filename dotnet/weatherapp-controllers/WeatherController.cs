using System.Net.Http;
using System.Text.Json;
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
        public async Task<ActionResult<WeatherReport>> GetWeather()
        {
            var client = _factory.CreateClient();
            var bytes = await client.GetByteArrayAsync("http://localhost:5002/forecast");
            var forecast = JsonSerializer.Deserialize<WeatherForecast>(bytes);
            return new WeatherReport() { Location = "Seattle", Forecast = forecast.Weather, };
        }
    }
}