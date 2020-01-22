$path = Join-Path $PSScriptRoot rps.json
$json = Get-Content -Raw -Path $path | ConvertFrom-Json
return $json