$path = Join-Path $PSScriptRoot scenarios.json
$json = Get-Content -Raw -Path $path | ConvertFrom-Json
$combinations = Invoke-Expression -Command (Join-Path $PSScriptRoot foreach-combination.ps1)

$results = @()
for ($i = 0; $i -lt $json.Count; $i++)
{
    for ($j = 0; $j -lt $combinations.Count; $j++)
    {
        $results += ([PSCustomObject]@{ 
            scenario = $json[$i] 
            cpu = $combinations[$j].cpu
            memory = $combinations[$j].memory })
    }
}

return $results;
