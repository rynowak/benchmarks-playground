$weather_url = $env:PERF_LINUX_URL
$forecast_url = $env:PERF_LOAD_URL
$load_url = $env:PERF_WINDOWS_URL
$server_url = $env:PERF_SERVER_URL

if (-not $forecast_url) {
    Write-Error "missing required environment variable"
    return 1
}

if (-not $weather_url) {
    Write-Error "missing required environment variable"
    return 1
}

if (-not $load_url) {
    Write-Host "missing required environment variable"
    return 1
}

if (-not $server_url) {
    Write-Host "missing required environment variable"
    return 1
}

$scenarios = Invoke-Expression -Command (Join-Path $PSScriptRoot foreach-scenario.ps1)

$i = 0;
$count = $scenarios.Count

foreach ($scenario in $scenarios) {
    $rps = "100"
    $cpu = "1.0"
    $memory = "500"
    $scaled_cpus = [System.Math]::Max([double]$scenario.cpu, 1.0)

    $i++
    Write-Host "Step $i of $count - $scenario"

    dotnet run -p c:\git\aspnet\benchmarks\src\BenchmarksDriver2 -- `
        --config "$PSScriptRoot\benchmarks.yaml" `
        --scenario $scenario `
        --load.endpoints $load_url `
        --forecast.endpoints $forecast_url `
        --forecast.options.displayBuild true `
        --forecast.options.displayOutput true `
        --weather.endpoints $weather_url `
        --weather.arguments "--cpus=$cpu -m=$memory" `
        --weather.environmentvariables "CPUS=$scaled_cpus" `
        --weather.options.displayBuild true `
        --weather.options.displayOutput true `
        --variable rps=$rps `
        --variable serverUri=$server_url
}