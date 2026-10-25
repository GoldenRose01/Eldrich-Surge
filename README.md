# Eldritch Surge

📚 **[Wiki completa](https://github.com/GoldenRose01/Eldrich-Surge/wiki)** — catalogo degli incantamenti, regole, tavolo avanzato, fusioni e configurazione.

**A Fabric enchantment overhaul for Minecraft 26.3**

Eldritch Surge expands enchantment progression and combat with **106 custom enchantments**, new enchantment groups, an Advanced Enchanting Table, Spell Books, special anvil recipes and world-specific configuration.

## Features

- 106 custom enchantments with defined levels, item categories, acquisition routes and rarity classes.
- Enchantment effects for combat, mining, armor, movement, loot, curses, blessings and spell casting.
- An Advanced Enchanting Table with separate progression and a required block formation for its 15- and 30-level slots.
- Spell Books as a distinct item type for cast-spell enchantments.
- Special anvil transformations, including the component enchantment combinations for Blade of Apocalypse and Star Fate, plus Katana to Gream Reaper.
- Optional EnchLib integration for editable mob categories, enchantment metadata, rarity and compatibility rules. EnchLib remains a separate mod and is not bundled in the Eldritch Surge jar.
- World-specific enchantment rules, with server-to-client synchronization. Operators with permission level 2 or higher can change these rules.

## Compatibility

| Component | Version / status |
|---|---|
| Minecraft | 26.3 |
| Mod | 1.6.0 |
| Mod loader | Fabric |
| Fabric API | Required |
| EnchLib | Optional; separate project and jar |
| Mod Menu + Cloth Config or YACL | Optional; either library enables the in-game config screen; choose when both are installed |

## Install

1. Install Fabric Loader for Minecraft 26.3.
2. Add Fabric API, Fabric Language Kotlin and the Eldritch Surge jar to the `mods` folder.
3. Add EnchLib separately if you want its mob-category and enchantment configuration integration.

The development build jar is stored under [`Jar/`](Jar/). Its filename follows `eldrich-surge-26.3+-1.6.0.jar`.

## Enchantment list and compatibility

See the [enchantment index](docs/wiki/Enchantments.md) for every registered enchantment, its maximum level, rarity/classification, supported item category, default source and declared exclusive set. Special component recipes and vanilla replacements are listed there too.

The [wiki home](docs/wiki/Home.md) links to configuration and gameplay documentation. The [test plan](docs/gameplay-test-plan.xlsx) tracks behavior that still needs in-game verification.

## World configuration

Global defaults are stored in `config/eldrich-surge/enchantment_caps.json`. Each save stores its overrides in `eldrich-surge/enchantment_caps.json` within the world directory. The server sends the active snapshot to clients and accepts changes only from operators at permission level 2 or above. See [World configuration](docs/wiki/Configuration.md).

## Development

External utilities and their assets live in [`external_tools/`](external_tools/README.md). The configurator starts from `external_tools/config-tool/config.exe`.

Standalone regression checks are registered centrally in `gradle/verification.gradle.kts`; add a task name and entry point there to include a new check in `gradlew check`.

Build with `gradlew build`. EnchLib is loaded as an optional separate runtime dependency when its jar is available in the configured sibling project; it is never shaded into Eldritch Surge.

## License

MIT.
