using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

namespace weatherapp_route_to_code
{
    public class Program
    {
        public static void Main(string[] args)
        {
            CreateHostBuilder(args).Build().Run();
        }

        public static IHostBuilder CreateHostBuilder(string[] args) =>
            Host.CreateDefaultBuilder(args)
                .ConfigureWebHostDefaults(webBuilder =>
                {
                    webBuilder.UseSockets(options =>
                    {
                        Console.WriteLine("IOCOUNT ORIGINAL VALUE YO: " + options.IOQueueCount);

                        if (int.TryParse(System.Environment.GetEnvironmentVariable("IOQUEUECOUNT"), out var count))
                        {
                            options.IOQueueCount = count;

                            Console.WriteLine("IOCOUNT NEW VALUE YO: " + options.IOQueueCount);
                        }
                    });
                    webBuilder.UseStartup<Startup>();
                });
    }
}
