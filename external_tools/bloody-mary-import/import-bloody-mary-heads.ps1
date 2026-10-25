param([Parameter(Mandatory=$true)][string]$ZipPath)
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem
$destination = Join-Path $PSScriptRoot '../../src/main/resources/data/eldrich-surge/loot_table/bloody_mary'
function Convert-HeadNode($node) {
    if ($node -isnot [System.Collections.IDictionary]) { return }
    if ($node.type -match '^(minecraft:)?random_chance') {
        $node.Clear()
        $node.type = 'minecraft:all_of'
        $node.terms = @()
        return
    }
    if ($node.ContainsKey('type_specific') -and $node.type_specific.type -eq 'minecraft:sheep') {
        $node.Remove('type_specific')
    }
    foreach ($key in @($node.Keys)) {
        $value = $node[$key]
        if ($key -eq 'minecraft:profile' -and $value.properties) {
            $hash = [System.Security.Cryptography.MD5]::HashData([System.Text.Encoding]::UTF8.GetBytes($value.properties[0].value))
            $value.id = @(0,4,8,12 | ForEach-Object { [BitConverter]::ToInt32($hash,$_) })
            $value.name = 'BloodyMary'
        }
        if ($value -is [System.Collections.IList]) {
            foreach ($child in $value) { Convert-HeadNode $child }
        } else { Convert-HeadNode $value }
    }
}
$archive = [System.IO.Compression.ZipFile]::OpenRead((Resolve-Path -LiteralPath $ZipPath))
try {
    # The archive declares this overlay active for Minecraft 26.3's data format 121.0.
    foreach ($prefix in @('data/more_mob_heads/loot_table/', 'overlay_107_1/data/more_mob_heads/loot_table/')) {
        foreach ($entry in $archive.Entries | Where-Object { $_.FullName.StartsWith($prefix) -and $_.FullName.EndsWith('.json') }) {
            $relative = $entry.FullName.Substring($prefix.Length)
            if ($relative.Contains('..')) { throw 'Unsafe archive entry' }
            $reader = [System.IO.StreamReader]::new($entry.Open())
            try { $table = $reader.ReadToEnd() | ConvertFrom-Json -AsHashtable } finally { $reader.Dispose() }
            Convert-HeadNode $table
            $path = Join-Path $destination $relative
            New-Item -ItemType Directory -Force (Split-Path $path) | Out-Null
            $json = ($table | ConvertTo-Json -Depth 100).Replace('more_mob_heads:', 'eldrich-surge:bloody_mary/')
            [System.IO.File]::WriteAllText($path, $json.Replace("`r`n", "`n") + "`n")
        }
    }
} finally { $archive.Dispose() }
