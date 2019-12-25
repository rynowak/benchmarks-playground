# What is this

Some basic benchmarks across different cloud-native stacks for testing performance under resource limits. I'm specifically investigating how different tech stacks behave when using CPU or memory limits in k8s (`--cpus` or `-m` in Docker).

There are other good benchmarks like TechEmpower that compare web stacks with the goal of maximum throughput with access to big hardware. I wanted to do my own benchmarking exercise that would give a different perspective focused on density and efficiency.

For this purpose of this testing, I'm making the assumption that you're invested in an orchestrator like kubernetes, and you want to partion you apps in such a way that kubernetes can dynamically scale them for you. Deciding the resource requirements of a single 'unit' of the application is a necessary step.

From the point of view of an ASP.NET Core framework/platform developer, I'm tying to do an exercise that will help understand:

- How is ASP.NET Core positioned vs other mainstream cloud-native web stacks?
- What guidance should we give ASP.NET Core developers that want to optimize for density?
- What gaps does .NET/ASP.NET Core have that the team should address?

## What does the app do

The app is a tiny REST API. It serves a really small JSOM payload. It also makes an outgoing HTTP call to itself.

Why do all of this? This set of things is designed to include features that are critical to the microservices programming style (for REST/JSON).

- Incoming HTTP
- JSON Serialization
- Outgoing HTTP
- Threading model (async in .NET)

Why not build a more realistic app, with real data access? Well, increasing the size and scope of what's being tested decreases the chance that we get something wrong.

## How I wrote these

I'm trying to understand where you land by writing applications using various **mainstream** tech stacks and using **mainstream** practices. If your team had to write 30 services in stack XYZ how would you write them?

I specifically want to avoid doing anything esoteric in the code, or doing lots of scenario-specific tuning in the app/config. I want these to honestly reflect the common practices.

[Techempower](https://www.techempower.com/benchmarks/) exists to serve as a benchmarking competion, the code samples here are intended to track more realistic code samples.

## What scenarios are tested

The apps here are tested across two primary use cases:

- Given a combination of CPU and memory - what's the maximum possible throughput in requests-per-second?
- Given a combination of CPU and memory - what does CPU usage, memory consumption, and latency look like with different fixed amounts of requests-per-second?

Each of these things tells us something separate. The max throughput test tells us what's possible in that configuration, and gives us some ability to extrapolate outside of the exact scenario tested. The fixed throughput test tells us about how to choose CPU and memory values based on on an application's needs.

---

As for the actual combinations of CPU and memory, these are informed by:

- Observation of requests/limits used by kubernetes infrastructure
- SKUs of Azure VM available and their ratios

---

Looking through the `kube-system` namespace you can find containers that request as low as `10m` CPU(`--cpus="0.01"`) and `10mi` memory. You'll also see cases where CPU limits are as low as `1.0` and memory limits are less than `200mi`.

We can draw a few conclusions from this:

- Clearly there's a bit of a range in terms of what's allocated to these infrastructure bits, so we're going to test a range.
- The target resource requests/limits for an infrastructure piece (kubernetes operator, daemon, or webhook) is going to be lower than a traditional app, because the scalability expectations are lower.
- This is a case where you generally want to optimize for efficiency over raw throughput.

note: For the purpose of eliminating some subtlety, I'm not making a distinction in testing between *requests* and *limits*. For a piece of infrastructure you want to make your requests as low as possible - instead of trying to design experiments around requests, we're making the assumption that you might need to run with *only* the requested amount available, and limits are an effective way to test that.

**Combinations to test (CPU x Memory):**

- `0.25 x 30m`
- `0.25 x 50m`
- `0.25 x 100m`
- `0.5 x 50m`
- `0.5 x 100m`
- `0.5 x 200m`
- `1.0 x 100m`
- `1.0 x 200m`
- `1.0 x 500m`

---

Doing analysis across [Azure's VM offerings](https://docs.microsoft.com/en-us/azure/virtual-machines/linux/sizes?toc=%2fazure%2fvirtual-machines%2flinux%2ftoc.json) should help us understand what resources are available and in what ratios. Different VM skus offer different tradeoffs, ratios, and pricing, so it makes sense that users would want to accept different tradeoffs for different apps.

VM size/groups that seemed relevant as *examplars* for this analysis were the A, B, D, and F. From examining these VMs we can determine what combinations make most sense to test.

note: This is a reminder that I'm not that interested in testing what happens when you allocate a really large amount of resources to a single instance.

### General Purpose

The [D-series](https://docs.microsoft.com/en-us/azure/virtual-machines/linux/sizes-general) (all of them) are the standard "general purpose" VM. The various options within the -series offer different choices of processor.

**CPU/Memory ratio:** 1 CPU / 3.5 GiB - 1 CPU / 4 GiB

**Size range in CPUs:** 1 - 96 CPUs

### General Purpose - non-hardware-specific

The [Av2-series](https://docs.microsoft.com/en-us/azure/virtual-machines/linux/sizes-general#av2-series) doesn't provide any guarantees about what physical hardware you're running upon. The advantage of this is in [the price](https://azure.microsoft.com/en-us/pricing/details/virtual-machines/linux/). For instance an A2v2 comes in at $0.076/hour where a comparable F2s2v is $0.085/hour.

The Azure docs mention that this VM series is most suitable for test/staging environments or low traffic web servers.

**CPU/Memory ratio:** 1 CPU / 2 GiB - 1 CPU / 8 GiB

**Size range in CPUs:** 1 - 8 CPUs

### General Purpose - bustable

The [B-series](https://docs.microsoft.com/en-us/azure/virtual-machines/linux/sizes-general#b-series) is a VM choice optimized for workloads that have bursty workloads. B-series VMs are cost effective when the application's CPU usage is low and predictable most of the time.

**CPU/Memory ratio:** 1 CPU / 0.5 GiB - 1 CPU / 4 GiB

**Size range in CPUs:** 1 - 20 CPUs

note: The B-series VM is optimized for a usage ratio like 0.1 CPU / 1 GiB in non-burst mode. This is a valuable point of comparison for testing workloads with predicatable RPS - how well does an app fit into the non-burst requirements of the B-series.

### Compute Optimized

Azure's docs describe the [Fsv2-series](https://docs.microsoft.com/en-us/azure/virtual-machines/linux/sizes-compute#fsv2-series-1) as the "compute optimized" choice - the most efficient way to pay for access to cores if that's you're limiting resource.

**CPU/Memory ratio:** 1 CPU / 2 GiB

**Size range in CPUs:** 2 - 72 CPUs

### Conclusions

Looking across all of the VM types considered, it seems obvious we should test a CPU / Memory ratio of `1 CPU / 1.0 GiB`, `1 CPU / 2.0 GiB`, `1 CPU / 3.0 GiB`, and `1 CPU / 4.0 GiB`. I'm including the `1 / 1.0 GiB` because not every app is the same, if your *budget* is `1 CPU / 2 GiB` (F-series), then some apps will be over budget and some will be under.

Since we're targeting density scenarios where kubernetes scaling is used, we should cover CPU limit values of `0.5`, `1.0`, `2.0`, `4.0`. I'm going to consider anything larger than that to be *too big* for this test - via my own arbitrary criteria.

Looking at the special properties of the A-series or B-series can help pull out some qualitative conclusions after collecting data.

note: As a personal note, the CPU / Memory ratios are a bit expectation-shattering for me. The standard CPU sizes are more-generous with memory compared to what I expected. It's possible that in practice we're not going to see a difference when testing across all of these memory ratios.

**Combinations to test (CPU x Memory):**

- `0.5 x 0.5 GiB`
- `0.5 x 1 GiB`
- `0.5 x 1.5 GiB`
- `0.5 x 2 GiB`
- `1.0 x 1 GiB`
- `1.0 x 2 GiB`
- `1.0 x 3 GiB`
- `1.0 x 4 GiB`
- `2.0 x 2 GiB`
- `2.0 x 4 GiB`
- `2.0 x 6 GiB`
- `2.0 x 8 GiB`
- `4.0 x 4 GiB`
- `4.0 x 8 GiB`
- `4.0 x 12 GiB`
- `4.0 x 16 GiB`

## How is this tested

I'm using the ASP.NET Core benchmarking infrastructure: [aspnet/Benchmarks](https://github.com/aspnet/Benchmarks). The tools and infrastructure are OSS (anyone can deploy this), however access to our servers running it are not.

For the unfamiliar, our benchmarking tools run as a server, which can assign server jobs (running an application) and client jobs (running a load testing tool) to other computers. Our setup is used regularly to benchmark workloads that run millions of requests-per-second.

Our benchmarking system can run apps in a variety of .NET-centric ways as well as with containers. We're exclusively using containers for this test.

Our benchmarking system has wired up a bunch of well-known load-generation clients (`wrk`, `wrk2`, Bombardier), as well as some of our own invention.

### Procedure: Testing max requests-per-second

We can use the `wrk` client to test more than enough load to overwhelm any of these applications. However, it's not our goal to overwhelm the application, but to produce the best result. In order to do that we need to tune the number of requests generated by `wrk` by determining the optimial number of connections to use.

So, for each data point we want to produce (app x CPU x memory) we need to do test runs with increasing numbers of connections until we find the sweet spot. There's no trick to this, it just involves running a lot of iterations.

A typical trial for testing connections looks like:

- Deploy app
- Run a 30s warmup with a single connection
- Run a 15s *test run* with the specified number of connections

The `wrk` client can generate thousands of requests per second on a single connection. We're using a 30 second warmup to be conservative, by the time the *test run* starts, a .NET or Java app will have already served thousands of requests even if it's only handling a hundred per second.

Once we have the optimal number of connections it's time to do a real run.

- Deploy app
- Run a 10s warmup
- Run a 15s *test run*

As a sanity check, we're looking for all of these workloads to use all of the CPU time available (limited by CPU limit) when testing for maximum throughput.

### Procedure: Testing fixed requests-per-second

## FAQ

**Q: What hardware are you using to test?**

I'm

**Q: Using --cpus=1.0 is going to have bad performance in my stack of choice because it will prevent the GC from running in parallel with user code.**

**A:** This is a misunderstanding of what `--cpus` does. Docker has a [*variety*](https://docs.docker.com/config/containers/resource_constraints/#cpu) of cpu-related settings.

`--cpus` limits the proportion of CPU time available to the app. If the maching has 12 logical CPUs, then setting `--cpus=1.0` means the application gets 1/12 of the available CPU time aggregated across some quantum of time. This does not make the app "single threaded" - this is no correspondence between `--cpus` and the *effective concurrency* of the app.

**Q: My stack of choice has better frameworks/runtimes/options that are more optimized for this use-case.**

**A:** PRs welcome :) as are independent validations of my results.

The question in my mind when you give this feedback is - are these other options *mainstream choices?* Would a normal developer in a normal workplace adopt this solution in production? Does the technology maker say that it's ready for production use?

**Q: You're doing something suboptimal or inefficient. Plz fix.**

**A:** Please point out the error, or a send a PR so I can fix it and collect more data. However, I'm going to ask for some kind of documentation or guidance about this practice and why it's wrong (could be a blog article, or a github issue somewhere other than my repo).

Remember that my stated goal is to capture the experiences that mainstream users will have. I'm not logging to micro-optimize all of these samples, and I'm trying to stick to the defaults where the defaults are reasonable.

An example of this would be ineffiecient use of `HttpClient` in .NET. Application authors have to have a strategy to avoid over-allocating `HttpClient` instances. `RestTemplate` in Spring Boot has similar issues. If I'm making this kinds of mistakes then I want to fix them.

---

The kind of thing I won't compromise on would be something like changing `JsonSerializer.Serialize(...)` to write a hardcoded set of bytes to the output.

In many ways a benchmark is always a facsimile of a real application. I'm going to keep the idiomatic patterns of a real application in place to maintain that connection.

---

Ultimately this is my repo, my goals, my time spent collecting data, and my credibility associated with any conclusions I draw - so every judgement call (and there are a lot of them) on every decision. If you think I'm wrong there's no better case to make than one backed up with data.

**Q: Why doesn't this have a database?**

**A:** All benchmarks are imperfect, and you have to draw the line somewhere when making comparisons across tech stacks.

This seemed like a good place to draw the line. Different tech stacks have different affinities to different databases as well. I don't want to increase the surface area of what I'm testing more than necessary.
