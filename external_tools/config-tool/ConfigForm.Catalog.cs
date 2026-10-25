using System.IO.Compression;
using System.Text.Json;
using System.Text.Json.Nodes;
using System.Text.RegularExpressions;

namespace EldritchConfigTool;

internal sealed partial class ConfigForm
{
    private static readonly RarityOption[] RarityOptions =
    {
        new(10, "10 - Molto raro (Infinity)"),
        new(20, "20 - Raro (Looting)"),
        new(50, "50 - Comune (Knockback)"),
        new(100, "100 - Molto comune (Sharpness)")
    };

    private static readonly ItemChoice[] GroupChoices =
    {
        new("Swords", "#minecraft:swords", "diamond_sword", true),
        new("Axes", "#minecraft:axes", "diamond_axe", true),
        new("Pickaxes", "#minecraft:pickaxes", "diamond_pickaxe", true),
        new("Shovels", "#minecraft:shovels", "diamond_shovel", true),
        new("Hoes", "#minecraft:hoes", "diamond_hoe", true),
        new("Helmets", "#minecraft:head_armor", "diamond_helmet", true),
        new("Chestplates", "#minecraft:chest_armor", "diamond_chestplate", true),
        new("Leggings", "#minecraft:leg_armor", "diamond_leggings", true),
        new("Boots", "#minecraft:foot_armor", "diamond_boots", true),
        new("All armor", "#minecraft:enchantable/armor", "diamond_chestplate", true),
        new("Bow", "#minecraft:enchantable/bow", "bow", true),
        new("Crossbow", "#minecraft:enchantable/crossbow", "crossbow", true),
        new("Trident", "#minecraft:enchantable/trident", "trident", true),
        new("Mace", "#minecraft:enchantable/mace", "mace", true),
        new("Fishing rod", "#minecraft:enchantable/fishing", "fishing_rod", true),
        new("Weapons", "#minecraft:enchantable/weapon", "netherite_sword", true),
        new("Mining tools", "#minecraft:enchantable/mining", "diamond_pickaxe", true),
        new("Durability items", "#minecraft:enchantable/durability", "anvil", true)
    };

    private static readonly string[] WeaponEnchantments =
    {
        "Backslash", "Bane of End", "Blade of Apocalypse", "Butcher", "Catapult", "Creeping Threat",
        "Curse of Perish", "Curse of Spider", "Dash", "Dual Sweeping", "Exorcist", "Experienced",
        "Flogging", "Freeze Aspect", "Gream Reaper", "Grim Reaper", "Herbicide", "Inking", "Katana",
        "Launch", "Leeching Aspect", "Lunge", "Midas Touch", "Neptunes Will", "Payback", "Quick Hit",
        "Sea Breeze", "Smoother", "Star Fate", "Superweight", "Triumph", "Undead Slayer", "Void Sweep",
        "Witch Hunter", "Wrath of the Abyss", "Sharpness", "Smite", "Bane of Arthropods", "Knockback",
        "Fire Aspect", "Looting", "Sweeping Edge", "Impaling", "Density", "Breach", "Wind Burst"
    };

    private static readonly string[] DefenseEnchantments =
    {
        "Armored", "Engine", "Blessing of the Night", "End Blessing", "Hell Blessing", "Sun Blessing",
        "Vision Blessing", "Health Upgrade", "Heart of Depth", "Heart of Nether", "Heart of the Sea",
        "Heart of the Sky", "End Adaptability", "Healing Aura", "Hicker", "Ice Speed", "Snowshoeing",
        "Soft Falling", "Soil Falling", "Combustion Protection", "Dwarf Flakes", "End Flakes",
        "Hell Flakes", "Sea Flakes", "Dwarf Forged", "End Forged", "Nether Forged", "Villager Forged",
        "Weapon Protection", "Smithcrafts", "Protection", "Fire Protection", "Feather Falling",
        "Blast Protection", "Projectile Protection", "Respiration", "Aqua Affinity", "Thorns",
        "Depth Strider", "Frost Walker", "Soul Speed", "Swift Sneak"
    };

    private static readonly string[] ArcheryEnchantments =
    {
        "Clearmind", "Curse of Target", "Elasticity", "Nineleven", "Piercing", "Pop", "Replenish",
        "Sniper", "Theft", "Power", "Punch", "Flame", "Infinity", "Multishot", "Quick Charge"
    };

    private static readonly string[] ToolEnchantments =
    {
        "Digger", "Excavator", "Pruning", "Sickened of Hell", "Smelting", "Efficiency", "Fortune",
        "Silk Touch", "Luck of the Sea", "Lure"
    };

}
