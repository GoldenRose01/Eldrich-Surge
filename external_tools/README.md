# External tools

Each tool owns its code, assets, launch files and build instructions in a separate folder. Tools are independent of the mod's Gradle build.

## Configurator

Launch `config-tool/config.exe` on Windows with the .NET 10 Desktop Runtime installed. Keep its DLL, runtime JSON files and `mc_itemlist/` alongside it.

The configurator runs inside this checkout: it discovers the repository from its executable location, reads the mod resources and edits `run/config/eldrich-surge/enchantment_caps.json`. Starting it from another working directory is supported; moving it outside the checkout is not.

Rebuild with the .NET 10 SDK:

```powershell
./external_tools/config-tool/build.ps1
```

Source responsibilities:

- `Program.cs`: application entry point and categories.
- `ConfigForm.cs`: state and editing interactions.
- `ConfigForm.Layout.cs`: interface construction and category selection.
- `ConfigForm.Catalog.cs`: item groups, rarity options and classification names.
- `ConfigForm.Data.cs`: loading configuration, resources, icons and Minecraft items.
- `ConfigModels.cs`: configuration models and JSON conversion.

`bin/`, `obj/` and `publish/` are generated and excluded from Git.

## Bloody Mary import

Run `import-bloody-mary-heads.ps1 -ZipPath <archive.zip>` with PowerShell 7 to import loot tables into the mod resources. The destination is resolved relative to the script, independently of the current directory.
