# Eldritch Surge

**Expand your enchantment progression with 119 custom enchantments, advanced enchanting, Spell Books and configurable world rules.**

Eldritch Surge is a Fabric mod for Minecraft 26.3 & 26.4 Snapshot 8 that expands the way you build equipment and progress through enchantments. Discover new options for weapons, armor, tools, ranged equipment, shields and more, with effects spanning combat, exploration, mining, movement, survival and spell casting.

## 119 custom enchantments

Explore an enchantment collection that includes offensive abilities, defensive upgrades, environmental blessings, curses, utility effects and rituals. Each enchantment has its own level limits, supported equipment and default acquisition rules.

Build around weapon enchantments such as Katana, Freeze Aspect and Leeching Aspect; explore armor and movement options such as Health Upgrade, Rabbit Foot and Snowshoeing; or develop specialized tools with enchantments such as Digger, Excavator, Lumberjack and Smelting.

The collection also includes enchantments for bows and crossbows, shields, maces, spears and shulker boxes. Item compatibility varies by enchantment, so different equipment types support different combinations.

## Advanced enchanting

The Advanced Enchanting Table adds another stage to enchantment progression. It uses Amethyst Shards, persistent block entity storage (dropping reagents safely upon destruction), and a dedicated block formation, with higher-level options available only when the required formation is valid.

XP costs scale based on the enchantment's rarity class (Common, Uncommon, Rare, Epic, Ultra). Normal and advanced table availability are configured separately. This lets a world distinguish everyday upgrades from enchantments reserved for later progression. Treasure-only enchantments remain excluded from both tables unless specifically enabled in loot or world configuration.

## Commands and gamerules

Operators can configure gameplay behavior dynamically using `/eldrich <setting> <on/off>` (or `/eldritch`):
- `keepLoyaltyTridents`: Prevents Loyalty tridents from falling to the ground or disappearing into the void/on death.
- `sweepingRequiresEnchantment`: Restricts the sword sweeping attack to weapons equipped with Sweeping Edge.
- `lethalPoison`: Allows Poison effect damage to be lethal to `player`, `mob`, or `all` entities.

## Spell Books and rituals

Spell Books are a separate item type for cast-spell enchantments, extending enchanting beyond conventional equipment upgrades. The spell collection includes Cocktail Spell, Ragnarok, Red Moon, Storm Spell and Trench Spell, connected to the advanced enchanting workflow with Echo Shards.

## Special anvil combinations

Some enchantments have dedicated anvil transformations. These include the component combinations for Blade of Apocalypse and Star Fate, as well as the Katana-to-Gream Reaper transformation using a Nether Star.

These special results have their own acquisition and compatibility rules, giving players additional goals beyond ordinary enchanting-table rolls.

## Equipment choices and compatibility

Enchantment groups and exclusive combinations encourage specialized equipment builds. Supported item categories, level limits, acquisition routes and compatibility rules determine which enchantments can be combined and where they can appear.

The full enchantment index documents registered enchantments, maximum levels, default sources, supported items and declared exclusive groups.

## Configure each world

Each world keeps its own enchantment settings. Configure whether an enchantment can appear in the normal table, the Advanced Enchanting Table or loot, and set independent maximum levels for anvils and enchanting tables.

A level value of 0 uses the registered vanilla or datapack limit. Global defaults provide the starting preset, while world overrides remain stored with the save.

On multiplayer servers, the server sends the active settings to connected clients. Editing requires operator permission level 2 or higher, and the server checks submitted changes before saving and synchronizing them.

## Choose Cloth Config or YACL

With Mod Menu installed, use either Cloth Config or YACL to access the in-game configuration screen. Eldritch Surge detects the installed library automatically. If both are available, you can choose which interface to open.

The Cloth Config interface organizes enchantments into tabs by source namespace, with collapsible sections, explanatory tooltips and validation for level fields. Both interfaces use the same underlying world configuration.

## Optional EnchLib integration

EnchLib adds integration for enchantment metadata, mob categories, rarity and compatibility rules through its supported API. Eldritch Surge and EnchLib are separate mods; EnchLib is optional and is not included in the Eldritch Surge download.

## Installation

1. Install Fabric Loader for Minecraft 26.3 or 26.4 Snapshot 8 and use Java 25.
2. Add Fabric API, Fabric Language Kotlin and Eldritch Surge to your mods folder.
3. Optionally add Mod Menu and Cloth Config or YACL for in-game configuration.
4. Optionally install EnchLib for its additional integrations.

**Current release:** 1.8.0 · **Minecraft:** 26.3 / 26.4 Snapshot 8 · **Loader:** Fabric · **License:** MIT
