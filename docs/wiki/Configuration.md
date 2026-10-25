# World configuration

## Where settings are stored

The default preset remains in the game configuration directory at `config/eldrich-surge/enchantment_caps.json`. When a world starts, Eldritch Surge loads its override file from the world save at `eldrich-surge/enchantment_caps.json`. Missing entries inherit the global defaults. Each world therefore keeps its own choices.

The world server sends the active configuration snapshot to each joining client. Client settings are read-only unless that player is authorized to edit the world. Changes submitted by a client are validated again on the server, saved to that world's file, and broadcast back to connected clients.

## Permissions

Changing rules requires **operator permission level 2 or higher** (Minecraft `COMMANDS_GAMEMASTER` and above). The Mod Menu controls are disabled for users below that level, and the server rejects unauthorized update packets.

## Rules currently exposed in Eldritch Surge

- Enable or disable an enchantment in the normal or Advanced Enchanting Table.
- Enable or disable its treasure/loot eligibility.
- Set independent anvil and enchanting-table maximum levels; `0` uses the registered vanilla/data-pack value.
- EnchLib-provided enabled state, max level, table chance, rarity class, item categories and incompatibilities are included in the server snapshot when available through its API.

Eldritch Surge and EnchLib are separate projects and jars. EnchLib is optional. Compatibility with a new EnchLib API is detected at runtime; unsupported calls fall back to Eldritch Surge's world values.

## Current integration note

The checked-out EnchLib project currently reports version 1.4.1. Its announced 1.5.0 world-configuration API is not present in this checkout yet, so the server-authoritative world storage and client synchronization described above are implemented by Eldritch Surge itself. The EnchLib 1.5.0-specific API handshake still needs to be verified when that version is available.

## In-game configuration screen

Install Mod Menu and either Cloth Config or YACL. The installed provider is detected automatically. When both are installed, the configuration button opens a choice between them. Neither library is required to load the mod. Additional configuration libraries require a dedicated screen adapter; there is no universal configuration-screen API.

The Cloth Config screen groups enchantments by namespace into tabs, with collapsible sections and non-negative level fields. Both providers use the same world settings and permission checks.
