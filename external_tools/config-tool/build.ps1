$ErrorActionPreference = 'Stop'
$publishDirectory = Join-Path $PSScriptRoot 'bin/publish'
dotnet publish (Join-Path $PSScriptRoot 'src/EldritchConfigTool.csproj') -c Release -o $publishDirectory
if ($LASTEXITCODE -ne 0) { throw 'Config tool build failed.' }
Copy-Item -LiteralPath (Join-Path $publishDirectory 'EldritchConfigTool.dll') -Destination (Join-Path $PSScriptRoot 'EldritchConfigTool.dll') -Force
foreach ($file in @('EldritchConfigTool.deps.json', 'EldritchConfigTool.runtimeconfig.json')) {
    $content = [IO.File]::ReadAllText((Join-Path $publishDirectory $file)).Replace("`r`n", "`n")
    [IO.File]::WriteAllText((Join-Path $PSScriptRoot $file), $content)
}
Copy-Item -LiteralPath (Join-Path $publishDirectory 'EldritchConfigTool.exe') -Destination (Join-Path $PSScriptRoot 'config.exe') -Force
