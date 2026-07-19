# Enchantment Implementation Status

This file tracks enchantments that currently have gameplay logic beyond registration/books/tooltips.

## Implemented

- `armored` - passive armor/resistance handling.
- `backslash` - applies Weakness on hit.
- `bane_of_end` - bonus damage against End-tagged mobs.
- `blade_of_apocalypse` - bonus damage based on configured entity taxonomy tags.
- `blessing_of_the_night` - night-time movement bonus.
- `butcher` - bonus damage against animal-tagged mobs.
- `catapult` - launches targets upward on critical hits.
- `clearmind` - removes blindness, nausea, and darkness.
- `combustion_protection` - fire resistance and fire clearing.
- `creeping_threat` - replaces Bane of Arthropods, damages/slows arthropods.
- `curse_of_spider` - applies poison on hit.
- `dash` - sprint speed bonus.
- `digger` - 3x3 mining.
- `end_adaptability` - End dimension resistance/slow falling.
- `end_blessing` - End dimension resistance/slow falling.
- `enduring` - dropped enchanted items receive unlimited lifetime.
- `excavator` - 3x3/5x5 area mining by level.
- `exorcist` - bonus damage against Nether-tagged mobs.
- `experienced` - critical-hit damage bonus.
- `flogging` - bonus damage against rebel-tagged mobs.
- `freeze_aspect` - slowness and freeze ticks on hit.
- `gream_reaper` - flat weapon damage bonus.
- `healing_aura` - sneaking aura heals nearby players.
- `health_upgrade` - health boost.
- `heart_of_depth` - water breathing and swimming bonus.
- `heart_of_nether` - Nether fire resistance/strength.
- `heart_of_the_sea` - water breathing and swimming bonus.
- `heart_of_the_sky` - slow falling.
- `hell_blessing` - Nether fire resistance/strength.
- `herbicide` - bonus damage against fungi-tagged mobs.
- `hicker` - jump boost.
- `ice_speed` - movement speed while grounded.
- `inking` - darkness, glowing, and weakness on hit.
- `katana` - replaces Sharpness, flat weapon damage bonus.
- `launch` - launches targets upward.
- `leeching_aspect` - heals attacker on critical hits.
- `lunge` - strong knockback/lunge effect.
- `midas_touch` - gold drop chance on kill.
- `payback` - bonus damage when attacker health is low.
- `refill` - enchanted shulker refills the held stack.
- `smelting` - block drops are replaced with smelted outputs.
- `smoother` - bonus damage against cubic-tagged mobs.
- `soft_falling` - slow falling.
- `sun_blessing` - daylight strength bonus.
- `superweight` - heavier damage bonus.
- `triumph` - heals attacker on kill.
- `undead_slayer` - replaces Smite, bonus damage against undead-tagged mobs.
- `vacuum` - enchanted shulker vacuums nearby item entities.
- `vision_blessing` - night vision.
- `weapon_protection` - reduces incoming weapon damage.
- `witch_hunter` - bonus damage against magik-tagged mobs.
- `wrath_of_the_abyss` - bonus damage against water-tagged mobs.

## Not Implemented Yet

- `cocktail_spell`
- `comet_slam`
- `curse_of_fragility`
- `curse_of_perish`
- `curse_of_target`
- `curse_of_the_forest`
- `deathbreak`
- `dual_sweeping`
- `dwarf_flakes`
- `dwarf_forged`
- `elasticity`
- `end_flakes`
- `end_forged`
- `engine`
- `gravity_well`
- `heartseeker`
- `heavy_impact`
- `hell_flakes`
- `impaler_reach`
- `neptunes_will`
- `nether_forged`
- `nineleven`
- `phalanx_stance`
- `piercing`
- `pop`
- `pruning`
- `quick_hit`
- `ragnarok`
- `red_moon`
- `replenish`
- `sea_breeze`
- `sea_flakes`
- `seismic_wave`
- `sickened_of_hell`
- `siphon`
- `smithcrafts`
- `sniper`
- `snowshoeing`
- `soil_falling`
- `soulbond`
- `star_fate`
- `storm_spell`
- `theft`
- `trench_spell`
- `vanguard_charge`
- `villager_forged`
- `void_sweep`
- `welding`

## Vanilla Replacements

- `minecraft:sharpness` is replaced by `eldritch-surge:katana`.
- `minecraft:smite` is replaced by `eldritch-surge:undead_slayer`.
- `minecraft:bane_of_arthropods` is replaced by `eldritch-surge:creeping_threat`.

The replaced vanilla enchantments are disabled by default in the Eldritch Surge config.
