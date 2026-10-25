using System.IO.Compression;
using System.Text.Json;
using System.Text.Json.Nodes;
using System.Text.RegularExpressions;

namespace EldritchConfigTool;

internal sealed partial class ConfigForm
{
    private void LoadConfig()
    {
        enchantments.Clear();
        Directory.CreateDirectory(Path.GetDirectoryName(configPath)!);

        foreach (string id in DiscoverEnchantments())
        {
            enchantments[id] = EnchantmentConfig.DefaultFor(id);
        }

        if (File.Exists(configPath))
        {
            var root = JsonNode.Parse(File.ReadAllText(configPath))?.AsObject() ?? new JsonObject();
            var entries = root["enchantments"]?.AsObject();
            if (entries != null)
            {
                foreach (var kv in entries)
                {
                    enchantments[kv.Key] = EnchantmentConfig.FromJson(kv.Value?.AsObject(), enchantments.TryGetValue(kv.Key, out var defaults) ? defaults : null);
                }
            }
        }

        foreach (string id in enchantments.Keys.ToList())
        {
            ApplyDatapackDefaults(id, enchantments[id]);
        }
    }

    private SortedSet<string> DiscoverEnchantments()
    {
        var ids = new SortedSet<string>(StringComparer.OrdinalIgnoreCase);
        string dataPath = Path.Combine(rootPath, "src", "main", "resources", "data");
        if (Directory.Exists(dataPath))
        {
            foreach (string file in Directory.EnumerateFiles(dataPath, "*.json", SearchOption.AllDirectories))
            {
                string normalized = file.Replace('\\', '/');
                Match match = Regex.Match(normalized, @"/data/([^/]+)/enchantment/(.+)\.json$");
                if (match.Success)
                {
                    ids.Add($"{match.Groups[1].Value}:{match.Groups[2].Value}");
                }
            }
        }

        string assetsPath = Path.Combine(rootPath, "src", "main", "resources", "assets");
        if (Directory.Exists(assetsPath))
        {
            foreach (string langFile in Directory.EnumerateFiles(assetsPath, "*.json", SearchOption.AllDirectories))
            {
                if (!langFile.Replace('\\', '/').Contains("/lang/")) continue;
                using JsonDocument doc = JsonDocument.Parse(File.ReadAllText(langFile));
                foreach (JsonProperty property in doc.RootElement.EnumerateObject())
                {
                    Match match = Regex.Match(property.Name, @"^enchantment\.([^.]+)\.(.+)$");
                    if (match.Success)
                    {
                        ids.Add($"{match.Groups[1].Value}:{match.Groups[2].Value.Replace('.', '/')}");
                    }
                }
            }
        }

        return ids;
    }

    private void ApplyDatapackDefaults(string id, EnchantmentConfig cfg)
    {
        string[] parts = id.Split(':', 2);
        if (parts.Length != 2) return;
        string file = Path.Combine(rootPath, "src", "main", "resources", "data", parts[0], "enchantment", parts[1] + ".json");
        if (!File.Exists(file)) return;

        JsonObject? obj = JsonNode.Parse(File.ReadAllText(file))?.AsObject();
        JsonNode? supported = obj?["supported_items"];
        if (supported is JsonValue value && value.TryGetValue<string>(out string? single) && cfg.SupportedItems.Count == 0)
        {
            cfg.DatapackSupportedItems.Add(single);
        }
        else if (supported is JsonArray array && cfg.SupportedItems.Count == 0)
        {
            foreach (JsonNode? node in array)
            {
                string? item = node?.GetValue<string>();
                if (!string.IsNullOrWhiteSpace(item)) cfg.DatapackSupportedItems.Add(item);
            }
        }
    }

    private void LoadItems()
    {
        itemChoices.Clear();
        itemImages.Images.Clear();

        foreach (ItemChoice group in GroupChoices)
        {
            itemChoices.Add(group);
            LoadIcon(group.IconKey);
        }

        var ids = new SortedSet<string>(StringComparer.OrdinalIgnoreCase);
        string assetsPath = Path.Combine(rootPath, "src", "main", "resources", "assets");
        if (Directory.Exists(assetsPath))
        {
            foreach (string file in Directory.EnumerateFiles(assetsPath, "*.json", SearchOption.AllDirectories))
            {
                string normalized = file.Replace('\\', '/');
                Match match = Regex.Match(normalized, @"/assets/([^/]+)/items/(.+)\.json$");
                if (match.Success) ids.Add($"{match.Groups[1].Value}:{match.Groups[2].Value}");
            }
        }

        foreach (string jarPath in CandidateMinecraftJars())
        {
            if (!File.Exists(jarPath)) continue;
            using var archive = ZipFile.OpenRead(jarPath);
            foreach (ZipArchiveEntry entry in archive.Entries)
            {
                Match match = Regex.Match(entry.FullName, @"^assets/minecraft/items/(.+)\.json$");
                if (match.Success) ids.Add($"minecraft:{match.Groups[1].Value}");
            }
            break;
        }

        foreach (string id in ids)
        {
            string itemName = id.Split(':', 2)[1];
            itemChoices.Add(new ItemChoice(id, id, itemName, false));
            LoadIcon(itemName);
        }
    }

    private void LoadIcon(string key)
    {
        if (itemImages.Images.ContainsKey(key)) return;
        string file = Path.Combine(iconPath, key + ".png");
        if (!File.Exists(file)) return;
        using Image original = Image.FromFile(file);
        itemImages.Images.Add(key, new Bitmap(original, itemImages.ImageSize));
    }

    private IEnumerable<string> CandidateMinecraftJars()
    {
        string userProfile = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile);
        yield return Path.Combine(userProfile, ".gradle", "caches", "fabric-loom", "minecraftMaven", "net", "minecraft", "minecraft-clientonly-deobf", "26.2", "minecraft-clientonly-deobf-26.2.jar");

        string loomCache = Path.Combine(rootPath, ".gradle", "loom-cache", "minecraftMaven", "net", "minecraft");
        if (!Directory.Exists(loomCache)) yield break;
        foreach (string jar in Directory.EnumerateFiles(loomCache, "minecraft-clientOnly-*.jar", SearchOption.AllDirectories))
        {
            yield return jar;
        }
    }

}
