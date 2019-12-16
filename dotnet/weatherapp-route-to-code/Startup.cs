using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

namespace weatherapp_route_to_code
{
    public class Startup
    {
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            
            app.UseRouting();

            app.UseEndpoints(endpoints =>
            {
                var client = new HttpClient();

                endpoints.MapGet("/", async context =>
                {
                    var forecast = await client.GetStringAsync("http://localhost:5000/forecast");
                    var report = new WeatherForecast()
                    {
                        Location = "Seattle",
                        Forecast = forecast,
                    };

                    context.Response.ContentType = "application/json";
                    await JsonSerializer.SerializeAsync(context.Response.Body, report);
                });

                endpoints.MapGet("/forecast", context =>
                {
                    context.Response.ContentType = "text/plain";
                    return context.Response.WriteAsync("Cloudy");
                });
            });
        }
    }
}
