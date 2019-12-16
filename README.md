# What is this

Some basic benchmarks across different cloud-native stacks for testing performance under resource limits. I'm specifically investigating how different tech stacks because when using CPU or memory limites in k8s (`--cpus` or `-m` in Docker).

## How is this tested

The `benchmarks.json` file can be used with [aspnet/Benchmarks](https://github.com/aspnet/Benchmarks). The ASP.NET Core team has this deployed for our own use (sorry, can't give access but the whole thing is OSS).

## How I wrote these

I'm trying to understand where you land by writing applications using various **mainstream** tech stacks and using **mainstream** practices. If your team had to write 30 services in stack XYZ how would you write them?

I specifically want to avoid doing anything esoteric in the code, or doing lots of scenario-specific tuning in the app/config. I want these to honestly reflect the common practices.

[Techempower](https://www.techempower.com/benchmarks/) exists to serve as a benchmarking competion, the code samples here are intended to track more realistic code samples.

## What does the app do

The app is a tiny REST API. It serves a really small JSOM payload. It also makes an outgoing HTTP call to itself.

Why do all of this? This set of things is designed to include features that are critical to the microservices programming style (for REST/JSON).

- Incoming HTTP
- JSON Serialization
- Outgoing HTTP
- Threading model (async in .NET)

Why not build a more realistic app, with real data access? Well, increasing the size and scope of what's being tested decreases the chance that we get something wrong.
