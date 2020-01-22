function Parse-Result([string] $filePath)
{
    $duration = 15; # in seconds
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
    $result.'Mean Latency (ms)' = [math]::Round(([double]$load.results.'bombardier/latency/mean') / 1000, 2)
    $result.'Completed Requests' = $load.results.'bombardier/requests'
    $result.'Completed Requests (%)' = [math]::Round(($load.results.'bombardier/requests' / ($duration * ([double]$json.properties.rps))) * 100, 2)
    $result.'Bad Responses' = $load.results.'bombardier/badresponses'
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