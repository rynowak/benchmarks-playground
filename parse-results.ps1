function Parse-Result([string] $filePath)
{
    $duration = 20; # in seconds
    $cpu_count = 12;

    $json = Get-Content -Raw -Path $filePath | ConvertFrom-Json;
    $weather = $json.jobs.weather;
    $load = $json.jobs.load

    $result = @{};

    $result.'Scenario' = $json.properties.Scenario
    $result.'RPS' = $json.properties.rps
    $result.'CPU Limit' = $json.properties.cpu
    $result.'Memory Limit (MB)' = $json.properties.memory.TrimEnd('M').TrimEnd('m')
    $result.'CPU Utilization %' = $weather.results.'benchmarks/cpu/raw'
    $result.'Max CPU Utilization %' = [math]::Round(([double]$json.properties.cpu) * (([double]100)), 2)
    $result.'Working Set (MB)' = $weather.results.'benchmarks/working-set'
    $result.'Swap (MB)' = $weather.results.'benchmarks/swap'

    $result.'Latency 50% (ms)' = [math]::Round(([double]$load.results.'wrk2/latency/50'), 2)
    $result.'Latency 75% (ms)' = [math]::Round(([double]$load.results.'wrk2/latency/75'), 2)
    $result.'Latency 90% (ms)' = [math]::Round(([double]$load.results.'wrk2/latency/90'), 2)
    $result.'Completed Requests' = $load.results.'wrk2/requests'
    $result.'Completed Requests (%)' = [math]::Round(($load.results.'wrk2/requests' / ($duration * ([double]$json.properties.rps))) * 100, 2)

    return [PSCustomObject]$result
}

function ParseTo-Csv([string] $directoryPath)
{
    $results = @()
    $files = Get-ChildItem "$directoryPath\*" -Include *.json -Exclude *-smoketest.json
    foreach ($file in $files)
    {
        $parsed = Parse-Result $file.FullName
        $results += $parsed
    }

    $results | Export-Csv -Path "results.csv"
}