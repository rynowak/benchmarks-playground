$path = Join-Path $PSScriptRoot combinations.json
$json = Get-Content -Raw -Path $path | ConvertFrom-Json
return $json
