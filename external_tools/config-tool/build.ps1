$ErrorActionPreference = 'Stop'
$publishDirectory = Join-Path $PSScriptRoot 'bin/publish'
dotnet publish (Join-Path $PSScriptRoot 'EldritchConfigTool.csproj') -c Release -o $publishDirectory
if ($LASTEXITCODE -ne 0) { throw 'Config tool build failed.' }
foreach ($file in @('EldritchConfigTool.dll', 'EldritchConfigTool.deps.json', 'EldritchConfigTool.runtimeconfig.json')) {
    Copy-Item -LiteralPath (Join-Path $publishDirectory $file) -Destination (Join-Path $PSScriptRoot $file) -Force
}
Copy-Item -LiteralPath (Join-Path $publishDirectory 'EldritchConfigTool.exe') -Destination (Join-Path $PSScriptRoot 'config.exe') -Force
