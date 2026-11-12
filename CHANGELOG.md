# Changelog

## 1.8.0

- **Soulbond Respawn Retention**: Fixed soulbound item loss on death respawns when `keepInventory` is false via `ServerPlayerRespawnMixin.java`; soulbound items are preserved directly in their original inventory slots upon respawn.
- **Double Application Removal**: Resolved duplicate JSON effects + Java execution across 5 enchantments:
  - `bane_of_end` and `undead_slayer`: cleared redundant JSON effects, computing exact scaling in `MobCategoryDamageMechanics.java`.
  - `smithcrafts`: removed duplicate Java attribute modifier; native offhand attribute modifier provides +1 armor per level.
  - `curse_of_target`: removed redundant projectile hit mixin; native JSON effect applies Glowing cleanly for 1 second per level.
  - `replenish`: removed redundant Java arrow refund chance; datapack `minecraft:ammo_use = 0.0` reliably handles special ammo preservation.
- **Commands and Gamerules**: Added unified `/eldrich <setting> <on/off>` and `/eldritch` command suite:
  - `keepLoyaltyTridents`: preserves Loyalty tridents across deaths and void drops.
  - `sweepingRequiresEnchantment`: enforces that sword sweeping attacks require Sweeping Edge.
  - `lethalPoison`: allows Poison damage to be lethal to `player`, `mob`, or `all` entities.
  - Removed deprecated unprivileged `/surge` command.
- **Table Reagent Storage & Drop on Break**: Reagent storage (Lapis, Amethyst, Echo Shards) is now persisted into block entity NBT (`saveAdditional` / `loadAdditional`), and `BlockEntityMixin` guarantees items are safely dropped into the world upon breaking the block instead of vanishing.
- **Deterministic Vanilla Table RNG**: Replaced `ThreadLocalRandom` in `EnchantmentCapsConfig` with the seed-based `RandomSource` from enchanting context to guarantee consistency between preview and execution.
- **Safe Mob Spawning**: Added `hasChunkAt` bounds verification to `SpellCastMechanics` for Ragnarok and Red Moon, preventing unintended chunk generation and bedrock ceiling spawns in the Nether.
- **Armored Elytra Balancing**: Aligned Armored runtime armor bonus on Elytra with its declared description (2 armor points per level, max 10 points at level V instead of 20).
- **Advanced Table Performance & XP Costs**:
  - Eliminated 20-ticks-per-second altar formation rescans in `AdvancedEnchantmentMenu`; structure checks now trigger only on container open and slot changes.
  - Infuser XP costs now dynamically scale by rarity class (Common: 15, Uncommon: 20, Rare: 25, Epic: 30, Ultra: 35 max XP).
- **Loot and Creative Categorization**: Enabled loot availability for `god_protection` and `gream_reaper`, and ensured `vanishing_curse` is categorized in the appropriate creative tab.
- **Snapshot Support**: Added compilation and release JARs for Minecraft 26.4 Snapshot 8 (`eldrich-surge-26.4-snapshot-8+-1.8.0.jar`).

## 1.7.1

- Real enchantment level caps shown in Advanced Table without requiring altar formation for view.
- Compact tooltips with hoverable detail panels in inventory and Advanced Table.
- Categorized enchantments across 15 effect tags.
- Added Fragility, Perish, Spider, Target, and The Forest to `minecraft:curse`.
- Added support for Minecraft 26.4 Snapshot 7.

## 1.7.0

- Added 14 new enchantments: Fanatic, Dodge, Fuse Cutter, Immortality, Curse of Slippery, Curse of Incompatibility, Curse of Fatigue, Curse of Corrosion, Blessing of the Sea, Accuracy Shot, Fear, Kinetic Protection, Happy Boost, Step Assist (total 118 enchantments).
- Dual-row armor HUD support for armor values beyond 20.
- Neptune's Will thrown trident bonus scaling and Dodge invulnerability frame mechanics.

## 1.6.2

- Reduced Swiftness movement bonus by 30%; it remains active only while charging a bow or spear.
- Moved Curse of Vanishing, Curse of Binding, Mending, and Unbreaking to the Extra creative tab.
- Added a second armor HUD row for armor above the normal 20 points.
- Neptune's Will now adds 5 damage per level with a trident and transfers the bonus to the main-hand weapon when the enchanted trident is in the offhand.
- Enchanted books show their compatible creative pages in blue in Survival; removed the redundant compatibility line.

## 1.6.1

- Enchanted books now show emoji-marked compatible item types based on the active EnchLib/world configuration.
- Gream Reaper now adds Smite-equivalent damage against every creature; Midas Touch now rolls gold drops reliably on entity death.
- Sickened of Hell now reads the tool recorded in crop loot context and drops baked potatoes.
- Void Sweep now guarantees its level-based damage on direct and sweep hits, extends the sweep radius, and keeps Knockback II.
- Expanded Smelting to smeltable block drops and shovels; concrete powder now becomes matching stained glass.
- Removed pre-enchanted sample tools from the creative tabs while keeping enchantment books available.
- Standardized the Fabric mod ID and all resource/enchantment namespaces as `eldrich-surge`.
- Katana now adds 1.25 attack damage per enchantment level and displays the bonus in item statistics and its tooltip.
- Removed duplicate manual damage additions for Katana and Neptune's Will; Neptune's Will continues to use its existing attack damage attribute.
- Kept independent weapon damage bonuses active when Blade of Apocalypse is equipped; category damage remains replaced by Blade of Apocalypse as intended by the enchantment incompatibility group.
- Added regression checks for Katana and Neptune's Will attack damage scaling and Katana tooltips in English and Italian.

- Updated Katana and Digger descriptions in Italian and English; creative enchantment tabs now sort by displayed name.

- Added the Advanced Enchanting Table crafting recipe and recipe-book unlock.
- Registered the five enchantments that were defined in Java but absent from the data pack, and corrected the Spirit Cast ID typo.
- Reclassified melee enchantments before tools in creative tabs and aligned Digger and Excavator category names with their supported items.
- EnchLib command IDs now resolve against the active enchantment registry, and inventory changes are synchronized to clients.
- Added catalog/translation consistency and creative-tab category checks.
- Fixed creative search indexing so enchantment tooltips no longer invoke font rendering from the background search thread.
- Bloody Mary now gains 10 percentage points of head-drop chance per Looting level; its tooltip explains the scaling.
- Added regression checks for Bloody Mary probability scaling and all sheep-color head routes.
- Bloody Mary heads now display the mob/variant name instead of “BloodyMary”; native skull drops also receive matching possessive names.
- Middle-clicking an item frame now copies the complete displayed stack, including stored enchantments on enchanted books.
