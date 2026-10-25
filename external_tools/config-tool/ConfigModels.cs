using System.IO.Compression;
using System.Text.Json;
using System.Text.Json.Nodes;
using System.Text.RegularExpressions;

namespace EldritchConfigTool;

internal sealed record RarityOption(int Value, string Label)
{
    public string ButtonText => Label.Split(' ')[0] + " rarita";
    public override string ToString() => Label;
}

internal sealed record ItemChoice(string Label, string Value, string IconKey, bool IsGroup)
{
    public override string ToString() => Label;
}

internal sealed class EnchantmentConfig
{
    public int AnvilMaxLevel { get; set; }
    public int EnchantingTableMaxLevel { get; set; }
    public bool NormalTable { get; set; }
    public bool AdvancedTable { get; set; }
    public bool Loot { get; set; }
    public int Rarity { get; set; } = 100;
    public List<string> SupportedItems { get; set; } = new();
    public List<string> IncompatibleEnchantments { get; set; } = new();
    public List<string> DatapackSupportedItems { get; } = new();

    public static EnchantmentConfig DefaultFor(string id) => new()
    {
        NormalTable = id.StartsWith("minecraft:", StringComparison.OrdinalIgnoreCase),
        AdvancedTable = id.StartsWith("eldrich-surge:", StringComparison.OrdinalIgnoreCase),
        Loot = !id.StartsWith("minecraft:", StringComparison.OrdinalIgnoreCase) && !id.StartsWith("eldrich-surge:", StringComparison.OrdinalIgnoreCase)
    };

    public static EnchantmentConfig FromJson(JsonObject? obj, EnchantmentConfig? defaults)
    {
        var cfg = defaults ?? new EnchantmentConfig();
        if (obj == null) return cfg;
        cfg.AnvilMaxLevel = (int?)obj["anvilMaxLevel"] ?? 0;
        cfg.EnchantingTableMaxLevel = (int?)obj["enchantingTableMaxLevel"] ?? 0;
        cfg.NormalTable = (bool?)obj["normalTable"] ?? cfg.NormalTable;
        cfg.AdvancedTable = (bool?)obj["advancedTable"] ?? cfg.AdvancedTable;
        cfg.Loot = (bool?)obj["loot"] ?? cfg.Loot;
        cfg.Rarity = (int?)obj["rarity"] ?? 100;
        cfg.SupportedItems = ReadList(obj["supportedItems"]);
        cfg.IncompatibleEnchantments = ReadList(obj["incompatibleEnchantments"]);
        return cfg;
    }

    public JsonObject ToJson() => new()
    {
        ["anvilMaxLevel"] = AnvilMaxLevel,
        ["enchantingTableMaxLevel"] = EnchantingTableMaxLevel,
        ["normalTable"] = NormalTable,
        ["advancedTable"] = AdvancedTable,
        ["loot"] = Loot,
        ["rarity"] = Rarity,
        ["supportedItems"] = new JsonArray(SupportedItems.Select(x => JsonValue.Create(x)).ToArray<JsonNode?>()),
        ["incompatibleEnchantments"] = new JsonArray(IncompatibleEnchantments.Select(x => JsonValue.Create(x)).ToArray<JsonNode?>())
    };

    private static List<string> ReadList(JsonNode? node)
    {
        if (node is not JsonArray arr) return new List<string>();
        return arr.Select(x => x?.GetValue<string>())
            .Where(x => !string.IsNullOrWhiteSpace(x))
            .Select(x => x!)
            .ToList();
    }
}
