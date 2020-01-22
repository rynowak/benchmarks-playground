$path = Join-Path $PSScriptRoot scenarios.json
$json = Get-Content -Raw -Path $path | ConvertFrom-Json
return $json