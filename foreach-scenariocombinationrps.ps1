$scenarios = Get-Content -Raw -Path (Join-Path $PSScriptRoot scenarios.json) | ConvertFrom-Json
$rps_values = Get-Content -Raw -Path (Join-Path $PSScriptRoot rps.json) | ConvertFrom-Json
$combinations = Invoke-Expression -Command (Join-Path $PSScriptRoot foreach-combination.ps1)

$results = @()
for ($i = 0; $i -lt $scenarios.Count; $i++)
{
    for ($j = 0; $j -lt $combinations.Count; $j++)
    {
        for ($k = 0; $k -lt $rps_values.Count; $k++)
        {
            $results += ([PSCustomObject]@{ 
                scenario = $scenarios[$i]
                rps = $rps_values[$k].rps
                connections = $rps_values[$k].connections
                threads = $rps_values[$k].threads
                cpu = $combinations[$j].cpu
                memory = $combinations[$j].memory })
        }
    }
}

return $results;
