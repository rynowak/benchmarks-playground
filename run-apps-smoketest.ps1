. ("$PSScriptRoot\parse-results.ps1")

$weather_url = $env:PERF_LINUX_URL
$forecast_url = $env:PERF_LOAD_URL
$load_url = $env:PERF_DB_URL

$forecast_service_url = $env:PERF_LOAD_SERVER_URL
$weather_server_url = $env:PERF_LINUX_SERVER_URL
$weather_server_port = $env:PERF_LINUX_SERVER_PORT

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

if (-not $weather_server_url) {
    Write-Host "missing required environment variable"
    return 1
}

if (-not $weather_server_port) {
    Write-Host "missing required environment variable"
    return 1
}

if (-not $forecast_service_url) {
    Write-Host "missing required environment variable"
    return 1
}

if (-not (Test-Path "$PSScriptRoot\results\"))
{
    mkdir "$PSScriptRoot\results" | Out-Null
}

$scenarios = Invoke-Expression -Command (Join-Path $PSScriptRoot foreach-scenario.ps1)

$i = 0;
$count = $scenarios.Count

foreach ($scenario in $scenarios) {
    $rps = "100"
    $cpu = "1.0"
    $memory = "1000M"
    $scaled_cpus = [System.Math]::Max([double]$scenario.cpu, 1.0)

    $i++
    Write-Host "Step $i of $count - $scenario"

    dotnet run -p c:\git\aspnet\benchmarks\src\BenchmarksDriver2 -- `
        --config "$PSScriptRoot\benchmarks.yaml" `
        --scenario $scenario `
        --warmup.endpoints $load_url `
        --warmup.options.displayOutput true `
        --load.endpoints $load_url `
        --load.variables.rate $rps `
        --load.variables.connections 32 `
        --load.variables.threads 32 `
        --load.options.displayOutput true `
        --forecast.endpoints $forecast_url `
        --forecast.options.displayBuild true `
        --forecast.options.displayOutput true `
        --weather.endpoints $weather_url `
        --weather.arguments "--cpus=$cpu -m=$memory" `
        --weather.environmentVariables "CPUS=$scaled_cpus" `
        --weather.environmentVariables "FORECAST_SERVICE_URI=$forecast_service_url" `
        --weather.environmentVariables "FORECAST_SERVICE_MP_REST_URL=$forecast_service_url" `
        --weather.options.displayBuild true `
        --weather.options.displayOutput true `
        --variable serverUri=$weather_server_url `
        --variable serverPort=$weather_server_port `
        --output "$PSScriptRoot\results\$scenario-smoketest.json" `
        --property "cpu=$cpu" `
        --property "memory=$memory" `
        --property "scenario=$scenario" `
        --property "rps=$rps"
}