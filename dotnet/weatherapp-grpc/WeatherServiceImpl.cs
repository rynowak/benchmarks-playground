using System;
using System.Threading.Tasks;
using Google.Protobuf.WellKnownTypes;
using Grpc.Core;
using Weather;

namespace weatherapp_grpc
{
    public class WeatherServiceImpl : WeatherService.WeatherServiceBase
    {
        private readonly ForecastService.ForecastServiceClient _client;

        public WeatherServiceImpl(Weather.ForecastService.ForecastServiceClient client)
        {
            _client = client;
        }

        public override async Task<WeatherResponse> GetWeather(Empty request, ServerCallContext context)
        {
            var response = await _client.GetForecastAsync(new Empty());

            return new WeatherResponse
            {
                Forecast = response.Weather,
                Location = "Seattle"
            };
        }
    }
}
