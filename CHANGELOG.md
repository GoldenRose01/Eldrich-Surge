# Changelog

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
