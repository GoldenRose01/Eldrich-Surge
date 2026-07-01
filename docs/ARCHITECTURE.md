# Eldritch Surge Architecture

## Data-driven enchantments

Minecraft 1.21+ enchantments are datapack registry entries. Eldritch Surge does
not create Java `Enchantment` subclasses for normal enchantment definitions.
New enchantments belong under:

```text
src/main/resources/data/eldritch-surge/enchantment/<name>.json
```

Code should keep `RegistryKey<Enchantment>` or `Identifier` handles and resolve
registry entries at use sites.

## Runtime level caps

`EnchantmentIndex` scans `Registries.ENCHANTMENT` and `EnchantmentCapsConfig`
persists optional cap overrides for every discovered enchantment. A value of
`0` means "use vanilla/datapack value".

Expected generated config:

```json
{
  "configVersion": 1,
  "enchantments": {
    "minecraft:sharpness": {
      "anvilMaxLevel": 0,
      "enchantingTableMaxLevel": 0
    }
  }
}
```

Anvil and enchanting-table mixins should call `EnchantmentLevelCaps` at the
point where vanilla compares or clamps levels.

## Exclusive enchantment groups

Groups are tag-driven:

```text
data/eldritch-surge/tags/enchantment/additional_damage.json
data/eldritch-surge/tags/enchantment/weapon_utility.json
data/eldritch-surge/tags/enchantment/aspect.json
data/eldritch-surge/tags/enchantment/offhand.json
```

The compatibility mixin should reject multiple enchantments inside the same
group. `offhand` should additionally reject enchantments from the other three
groups unless a datapack or config rule explicitly relaxes that later.

## Multi-category mob taxonomy

Entity categories are also tags, so one entity type can belong to several
categories. Example: `minecraft:wither_skeleton` appears in both `undead` and
`hell`, so damage code must iterate all matching tags and combine every matching
bonus instead of branching on a single vanilla mob group.
