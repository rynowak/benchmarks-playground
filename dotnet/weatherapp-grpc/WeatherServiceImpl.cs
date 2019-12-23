using System;
using System.Threading.Tasks;
using Google.Protobuf.WellKnownTypes;
using Grpc.Core;
using Weather;

namespace weatherapp_grpc
{
    public class WeatherServiceImpl : WeatherService.WeatherServiceBase
    {
        private readonly WeatherService.WeatherServiceClient _client;

        public WeatherServiceImpl(Weather.WeatherService.WeatherServiceClient client)
        {
            _client = client;
        }

        public override async Task<WeatherResponse> GetWeather(Empty request, ServerCallContext context)
        {
            var response = await _client.GetForecastAsync(new Empty());

            return new WeatherResponse
            {
                Forecast = response.Forecast,
                Location = "Seattle"
            };
        }

        public override Task<ForecastResponse> GetForecast(Empty request, ServerCallContext context)
        {
            return Task.FromResult(new ForecastResponse
            {
                Forecast = "Cloudy"
            });
        }
    }
}
