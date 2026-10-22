# Eldritch Surge 1.4.0 — Configuration UI Update

For Minecraft 26.3 · Fabric

## New and improved

- Added an optional Cloth Config screen alongside the existing YACL integration.
- Automatically detects which configuration library is installed.
- Added a provider selection screen when both Cloth Config and YACL are installed.
- Organized the Cloth Config screen into tabs by enchantment namespace, with collapsible enchantment sections and explanatory tooltips.
- Added non-negative validation for level fields in the Cloth Config screen.
- Added English and Italian messages explaining world editing and read-only access.
- Sorted enchantment entries by identifier for consistent navigation.

## Configuration behavior

Both interfaces edit the same world-specific settings: normal and advanced table availability, loot eligibility, anvil level caps and enchanting-table level caps. Existing server synchronization and operator permission checks remain in place. A level cap of 0 keeps the vanilla or datapack value.

## Requirements

- Minecraft 26.3 with Fabric Loader 0.19.3 or newer
- Java 25 or newer
- Fabric API and Fabric Language Kotlin
- Optional configuration UI: Mod Menu with Cloth Config, YACL, or both
- Optional integration: EnchLib, installed separately

Cloth Config and YACL are not bundled. Neither is required to load Eldritch Surge. Other configuration libraries require their own integration.

## Validation

The release build compiles and packages successfully. The new screens still require visual and interaction testing in Minecraft.
