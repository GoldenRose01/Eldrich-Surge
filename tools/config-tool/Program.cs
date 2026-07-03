using System.IO.Compression;
using System.Text.Json;
using System.Text.Json.Nodes;
using System.Text.RegularExpressions;

namespace EldritchConfigTool;

internal static class Program
{
    [STAThread]
    private static void Main()
    {
        ApplicationConfiguration.Initialize();
        Application.Run(new ConfigForm());
    }
}

internal sealed class ConfigForm : Form
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

    private readonly string rootPath;
    private readonly string configPath;
    private readonly string iconPath;
    private readonly Dictionary<string, EnchantmentConfig> enchantments = new(StringComparer.OrdinalIgnoreCase);
    private readonly List<ItemChoice> itemChoices = new();
    private readonly ImageList itemImages = new() { ImageSize = new Size(24, 24), ColorDepth = ColorDepth.Depth32Bit };

    private readonly ListBox enchantmentList = new();
    private readonly TextBox enchantmentSearch = new();
    private readonly CheckBox normalTable = new() { Text = "Tavolo normale", AutoSize = true };
    private readonly CheckBox advancedTable = new() { Text = "Advanced table", AutoSize = true };
    private readonly CheckBox loot = new() { Text = "Loot", AutoSize = true };
    private readonly ComboBox rarity = new() { DropDownStyle = ComboBoxStyle.DropDownList };
    private readonly ListView itemList = new() { View = View.Details, FullRowSelect = true, HideSelection = false, MultiSelect = false };
    private readonly TextBox itemSearch = new();
    private readonly TextBox supportedItems = new() { Multiline = true, ScrollBars = ScrollBars.Vertical };
    private readonly ComboBox incompatiblePicker = new() { DropDownStyle = ComboBoxStyle.DropDownList };
    private readonly TextBox incompatible = new() { Multiline = true, ScrollBars = ScrollBars.Vertical };
    private readonly NumericUpDown anvilCap = new() { Minimum = 0, Maximum = 255, Width = 80 };
    private readonly NumericUpDown tableCap = new() { Minimum = 0, Maximum = 255, Width = 80 };

    private readonly ComboBox groupPicker = new() { DropDownStyle = ComboBoxStyle.DropDownList };
    private readonly CheckedListBox groupEnchantments = new() { CheckOnClick = true };
    private readonly ComboBox groupRarity = new() { DropDownStyle = ComboBoxStyle.DropDownList };
    private readonly CheckBox groupNormalTable = new() { Text = "Tavolo normale", AutoSize = true };
    private readonly CheckBox groupAdvancedTable = new() { Text = "Advanced table", AutoSize = true };
    private readonly CheckBox groupLoot = new() { Text = "Loot", AutoSize = true };

    private bool loading;

    public ConfigForm()
    {
        Text = "Eldritch Surge Config";
        MinimumSize = new Size(1320, 820);
        StartPosition = FormStartPosition.CenterScreen;

        rootPath = FindProjectRoot();
        configPath = Path.Combine(rootPath, "run", "config", "eldritch-surge", "enchantment_caps.json");
        iconPath = Path.Combine(rootPath, "mc_itemlist");

        BuildUi();
        LoadConfig();
        LoadItems();
        RefreshEnchantmentList();
        RefreshItemList();
        RefreshGroupEnchantments();
        RefreshIncompatiblePicker();
    }

    private void BuildUi()
    {
        foreach (RarityOption option in RarityOptions)
        {
            rarity.Items.Add(option);
            groupRarity.Items.Add(option);
        }

        var main = new TableLayoutPanel { Dock = DockStyle.Fill, ColumnCount = 2, Padding = new Padding(12) };
        main.ColumnStyles.Add(new ColumnStyle(SizeType.Absolute, 350));
        main.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100));
        Controls.Add(main);

        main.Controls.Add(BuildEnchantmentSidebar(), 0, 0);

        var tabs = new TabControl { Dock = DockStyle.Fill };
        tabs.TabPages.Add(BuildPerEnchantmentTab());
        tabs.TabPages.Add(BuildPerGroupTab());
        main.Controls.Add(tabs, 1, 0);
    }

    private Control BuildEnchantmentSidebar()
    {
        var panel = new TableLayoutPanel { Dock = DockStyle.Fill, RowCount = 3 };
        panel.RowStyles.Add(new RowStyle(SizeType.Absolute, 38));
        panel.RowStyles.Add(new RowStyle(SizeType.Percent, 100));
        panel.RowStyles.Add(new RowStyle(SizeType.Absolute, 44));

        enchantmentSearch.PlaceholderText = "Cerca incantesimo...";
        enchantmentSearch.Dock = DockStyle.Fill;
        enchantmentSearch.TextChanged += (_, _) => RefreshEnchantmentList();
        panel.Controls.Add(enchantmentSearch, 0, 0);

        enchantmentList.Dock = DockStyle.Fill;
        enchantmentList.SelectedIndexChanged += (_, _) => LoadSelected();
        panel.Controls.Add(enchantmentList, 0, 1);

        var pathButton = new Button { Text = "Mostra file config", Dock = DockStyle.Fill };
        pathButton.Click += (_, _) => MessageBox.Show(configPath, "File config");
        panel.Controls.Add(pathButton, 0, 2);
        return panel;
    }

    private TabPage BuildPerEnchantmentTab()
    {
        var page = new TabPage("Per incantesimo");
        var root = new TableLayoutPanel { Dock = DockStyle.Fill, ColumnCount = 2, RowCount = 4, Padding = new Padding(12) };
        root.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 50));
        root.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 50));
        root.RowStyles.Add(new RowStyle(SizeType.Absolute, 88));
        root.RowStyles.Add(new RowStyle(SizeType.Percent, 52));
        root.RowStyles.Add(new RowStyle(SizeType.Percent, 30));
        root.RowStyles.Add(new RowStyle(SizeType.Absolute, 82));
        page.Controls.Add(root);

        root.Controls.Add(BuildAvailabilityBox(), 0, 0);
        root.Controls.Add(BuildLevelBox(), 1, 0);
        root.Controls.Add(BuildItemBrowser(), 0, 1);
        root.Controls.Add(Wrap("Oggetti/tag ammessi", supportedItems), 1, 1);
        root.Controls.Add(BuildIncompatibilityBox(), 0, 2);
        root.SetColumnSpan(root.GetControlFromPosition(0, 2)!, 2);
        root.Controls.Add(BuildPresetBox(), 0, 3);
        root.Controls.Add(BuildSaveButtons(), 1, 3);
        return page;
    }

    private Control BuildAvailabilityBox()
    {
        var flags = new FlowLayoutPanel { Dock = DockStyle.Fill, FlowDirection = FlowDirection.LeftToRight, WrapContents = false };
        flags.Controls.AddRange(new Control[] { normalTable, advancedTable, loot });
        normalTable.CheckedChanged += (_, _) => SaveSelectedFromUi();
        advancedTable.CheckedChanged += (_, _) => SaveSelectedFromUi();
        loot.CheckedChanged += (_, _) => SaveSelectedFromUi();
        return Wrap("Disponibilita", flags);
    }

    private Control BuildLevelBox()
    {
        rarity.Dock = DockStyle.Left;
        rarity.Width = 310;
        rarity.SelectedIndexChanged += (_, _) => SaveSelectedFromUi();

        var panel = new TableLayoutPanel { Dock = DockStyle.Fill, ColumnCount = 2 };
        panel.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 55));
        panel.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 45));
        panel.Controls.Add(rarity, 0, 0);

        var caps = new FlowLayoutPanel { Dock = DockStyle.Fill, WrapContents = false };
        caps.Controls.AddRange(new Control[] { Label("Anvil"), anvilCap, Label("Table"), tableCap });
        anvilCap.ValueChanged += (_, _) => SaveSelectedFromUi();
        tableCap.ValueChanged += (_, _) => SaveSelectedFromUi();
        panel.Controls.Add(caps, 1, 0);
        return Wrap("Rarita e livelli", panel);
    }

    private Control BuildItemBrowser()
    {
        var panel = new TableLayoutPanel { Dock = DockStyle.Fill, RowCount = 3 };
        panel.RowStyles.Add(new RowStyle(SizeType.Absolute, 36));
        panel.RowStyles.Add(new RowStyle(SizeType.Percent, 100));
        panel.RowStyles.Add(new RowStyle(SizeType.Absolute, 42));

        itemSearch.PlaceholderText = "Cerca item o categoria...";
        itemSearch.Dock = DockStyle.Fill;
        itemSearch.TextChanged += (_, _) => RefreshItemList();
        panel.Controls.Add(itemSearch, 0, 0);

        itemList.Dock = DockStyle.Fill;
        itemList.SmallImageList = itemImages;
        itemList.Columns.Add("Nome", 210);
        itemList.Columns.Add("Valore config", 260);
        itemList.SelectedIndexChanged += (_, _) => RefreshIncompatiblePicker();
        itemList.DoubleClick += (_, _) => AddSelectedItem();
        panel.Controls.Add(itemList, 0, 1);

        var buttons = new FlowLayoutPanel { Dock = DockStyle.Fill, WrapContents = false };
        var add = new Button { Text = "Aggiungi", Width = 120, Height = 32 };
        add.Click += (_, _) => AddSelectedItem();
        var remove = new Button { Text = "Pulisci campo", Width = 120, Height = 32 };
        remove.Click += (_, _) => supportedItems.Clear();
        buttons.Controls.Add(add);
        buttons.Controls.Add(remove);
        panel.Controls.Add(buttons, 0, 2);
        return Wrap("Browser item e categorie", panel);
    }

    private Control BuildIncompatibilityBox()
    {
        var panel = new TableLayoutPanel { Dock = DockStyle.Fill, RowCount = 2 };
        panel.RowStyles.Add(new RowStyle(SizeType.Absolute, 42));
        panel.RowStyles.Add(new RowStyle(SizeType.Percent, 100));

        var pickerRow = new TableLayoutPanel { Dock = DockStyle.Fill, ColumnCount = 2 };
        pickerRow.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100));
        pickerRow.ColumnStyles.Add(new ColumnStyle(SizeType.Absolute, 140));
        incompatiblePicker.Dock = DockStyle.Fill;
        var add = new Button { Text = "Aggiungi", Dock = DockStyle.Fill };
        add.Click += (_, _) => AddSelectedIncompatible();
        pickerRow.Controls.Add(incompatiblePicker, 0, 0);
        pickerRow.Controls.Add(add, 1, 0);
        panel.Controls.Add(pickerRow, 0, 0);

        incompatible.TextChanged += (_, _) => SaveSelectedFromUi();
        panel.Controls.Add(incompatible, 0, 1);
        return Wrap("Incompatibile con questi incantesimi", panel);
    }

    private Control BuildPresetBox()
    {
        var presets = new FlowLayoutPanel { Dock = DockStyle.Fill, WrapContents = true, AutoScroll = true };
        AddPreset(presets, "Solo vanilla", true, false, false);
        AddPreset(presets, "Solo advanced", false, true, false);
        AddPreset(presets, "Solo loot", false, false, true);
        AddPreset(presets, "Tutti e 3", true, true, true);
        foreach (RarityOption option in RarityOptions)
        {
            AddRarityPreset(presets, option);
        }
        return Wrap("Preset rapidi", presets);
    }

    private Control BuildSaveButtons()
    {
        var buttons = new FlowLayoutPanel { Dock = DockStyle.Fill, FlowDirection = FlowDirection.RightToLeft, WrapContents = false };
        var save = new Button { Text = "Salva", Width = 130, Height = 34 };
        save.Click += (_, _) => SaveFile();
        var reload = new Button { Text = "Ricarica", Width = 130, Height = 34 };
        reload.Click += (_, _) => { LoadConfig(); RefreshEnchantmentList(); RefreshGroupEnchantments(); RefreshIncompatiblePicker(); };
        buttons.Controls.Add(save);
        buttons.Controls.Add(reload);
        return buttons;
    }

    private TabPage BuildPerGroupTab()
    {
        var page = new TabPage("Per tool/gruppo");
        var root = new TableLayoutPanel { Dock = DockStyle.Fill, ColumnCount = 2, RowCount = 4, Padding = new Padding(12) };
        root.ColumnStyles.Add(new ColumnStyle(SizeType.Absolute, 380));
        root.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100));
        root.RowStyles.Add(new RowStyle(SizeType.Absolute, 76));
        root.RowStyles.Add(new RowStyle(SizeType.Absolute, 76));
        root.RowStyles.Add(new RowStyle(SizeType.Percent, 100));
        root.RowStyles.Add(new RowStyle(SizeType.Absolute, 62));
        page.Controls.Add(root);

        foreach (ItemChoice group in GroupChoices)
        {
            groupPicker.Items.Add(group);
        }
        groupPicker.SelectedIndex = 0;
        groupPicker.Dock = DockStyle.Fill;
        root.Controls.Add(Wrap("Tool o gruppo", groupPicker), 0, 0);

        groupRarity.Width = 310;
        groupRarity.SelectedIndex = 3;
        root.Controls.Add(Wrap("Rarita da applicare", groupRarity), 1, 0);

        var flags = new FlowLayoutPanel { Dock = DockStyle.Fill, WrapContents = false };
        flags.Controls.AddRange(new Control[] { groupNormalTable, groupAdvancedTable, groupLoot });
        root.Controls.Add(Wrap("Disponibilita da applicare", flags), 0, 1);
        root.SetColumnSpan(root.GetControlFromPosition(0, 1)!, 2);

        groupEnchantments.Dock = DockStyle.Fill;
        root.Controls.Add(Wrap("Incantesimi da modificare", groupEnchantments), 0, 2);
        root.SetColumnSpan(root.GetControlFromPosition(0, 2)!, 2);

        var buttons = new FlowLayoutPanel { Dock = DockStyle.Fill, FlowDirection = FlowDirection.RightToLeft, WrapContents = false };
        var applyGroup = new Button { Text = "Applica gruppo", Width = 150, Height = 34 };
        applyGroup.Click += (_, _) => ApplyGroupToCheckedEnchantments();
        var removeGroup = new Button { Text = "Rimuovi gruppo", Width = 150, Height = 34 };
        removeGroup.Click += (_, _) => RemoveGroupFromCheckedEnchantments();
        var checkAll = new Button { Text = "Seleziona tutti", Width = 150, Height = 34 };
        checkAll.Click += (_, _) => SetAllGroupChecks(true);
        var clear = new Button { Text = "Deseleziona", Width = 150, Height = 34 };
        clear.Click += (_, _) => SetAllGroupChecks(false);
        buttons.Controls.Add(applyGroup);
        buttons.Controls.Add(removeGroup);
        buttons.Controls.Add(checkAll);
        buttons.Controls.Add(clear);
        root.Controls.Add(buttons, 0, 3);
        root.SetColumnSpan(buttons, 2);
        return page;
    }

    private static Label Label(string text) => new() { Text = text, AutoSize = true, Padding = new Padding(0, 7, 6, 0) };

    private static Control Wrap(string title, Control child)
    {
        var group = new GroupBox { Text = title, Dock = DockStyle.Fill, Padding = new Padding(10) };
        child.Dock = DockStyle.Fill;
        group.Controls.Add(child);
        return group;
    }

    private void AddPreset(Control parent, string text, bool normal, bool advanced, bool lootOnly)
    {
        var button = new Button { Text = text, Width = 150, Height = 34 };
        button.Click += (_, _) =>
        {
            normalTable.Checked = normal;
            advancedTable.Checked = advanced;
            loot.Checked = lootOnly;
            SaveSelectedFromUi();
        };
        parent.Controls.Add(button);
    }

    private void AddRarityPreset(Control parent, RarityOption option)
    {
        var button = new Button { Text = option.ButtonText, Width = 150, Height = 34 };
        button.Click += (_, _) =>
        {
            rarity.SelectedItem = option;
            SaveSelectedFromUi();
        };
        parent.Controls.Add(button);
    }

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

    private void RefreshEnchantmentList()
    {
        string previous = enchantmentList.SelectedItem as string ?? "";
        string filter = enchantmentSearch.Text.Trim();
        enchantmentList.BeginUpdate();
        enchantmentList.Items.Clear();
        foreach (string id in enchantments.Keys.OrderBy(x => x))
        {
            if (filter.Length == 0 || id.Contains(filter, StringComparison.OrdinalIgnoreCase))
            {
                enchantmentList.Items.Add(id);
            }
        }
        enchantmentList.EndUpdate();
        enchantmentList.SelectedItem = previous;
        if (enchantmentList.SelectedIndex < 0 && enchantmentList.Items.Count > 0) enchantmentList.SelectedIndex = 0;
    }

    private void RefreshItemList()
    {
        string filter = itemSearch.Text.Trim();
        itemList.BeginUpdate();
        itemList.Items.Clear();
        foreach (ItemChoice choice in itemChoices)
        {
            if (filter.Length > 0 && !choice.Label.Contains(filter, StringComparison.OrdinalIgnoreCase) && !choice.Value.Contains(filter, StringComparison.OrdinalIgnoreCase))
            {
                continue;
            }

            var item = new ListViewItem(choice.Label);
            item.SubItems.Add(choice.Value);
            item.Tag = choice;
            if (itemImages.Images.ContainsKey(choice.IconKey)) item.ImageKey = choice.IconKey;
            item.Font = choice.IsGroup ? new Font(itemList.Font, FontStyle.Bold) : itemList.Font;
            itemList.Items.Add(item);
            if (itemList.Items.Count >= 700) break;
        }
        itemList.EndUpdate();
    }

    private void RefreshGroupEnchantments()
    {
        groupEnchantments.BeginUpdate();
        groupEnchantments.Items.Clear();
        foreach (string id in enchantments.Keys.OrderBy(x => x))
        {
            groupEnchantments.Items.Add(id, false);
        }
        groupEnchantments.EndUpdate();
    }

    private void RefreshIncompatiblePicker()
    {
        string? selectedContext = SelectedItemChoice()?.Value ?? ParseList(supportedItems.Text).FirstOrDefault();
        string current = enchantmentList.SelectedItem as string ?? "";

        incompatiblePicker.BeginUpdate();
        incompatiblePicker.Items.Clear();
        foreach (string id in enchantments.Keys.OrderBy(x => x))
        {
            if (id.Equals(current, StringComparison.OrdinalIgnoreCase)) continue;
            if (selectedContext == null || EnchantmentCanUseContext(enchantments[id], selectedContext))
            {
                incompatiblePicker.Items.Add(id);
            }
        }
        incompatiblePicker.EndUpdate();
        if (incompatiblePicker.Items.Count > 0) incompatiblePicker.SelectedIndex = 0;
    }

    private bool EnchantmentCanUseContext(EnchantmentConfig cfg, string context)
    {
        List<string> supported = cfg.SupportedItems.Count > 0 ? cfg.SupportedItems : cfg.DatapackSupportedItems;
        if (supported.Count == 0) return true;
        return supported.Any(entry => EntryMatchesContext(entry, context));
    }

    private static bool EntryMatchesContext(string entry, string context)
    {
        if (entry.Equals(context, StringComparison.OrdinalIgnoreCase)) return true;
        if (entry.StartsWith("#") && context.StartsWith("#")) return entry.Equals(context, StringComparison.OrdinalIgnoreCase);
        if (entry.StartsWith("#minecraft:swords") && context.EndsWith("_sword", StringComparison.OrdinalIgnoreCase)) return true;
        if (entry.StartsWith("#minecraft:axes") && context.EndsWith("_axe", StringComparison.OrdinalIgnoreCase)) return true;
        if (entry.StartsWith("#minecraft:pickaxes") && context.EndsWith("_pickaxe", StringComparison.OrdinalIgnoreCase)) return true;
        if (entry.StartsWith("#minecraft:chest_armor") && context.EndsWith("_chestplate", StringComparison.OrdinalIgnoreCase)) return true;
        if (entry.StartsWith("#minecraft:head_armor") && context.EndsWith("_helmet", StringComparison.OrdinalIgnoreCase)) return true;
        if (entry.StartsWith("#minecraft:leg_armor") && context.EndsWith("_leggings", StringComparison.OrdinalIgnoreCase)) return true;
        if (entry.StartsWith("#minecraft:foot_armor") && context.EndsWith("_boots", StringComparison.OrdinalIgnoreCase)) return true;
        return false;
    }

    private void LoadSelected()
    {
        if (enchantmentList.SelectedItem is not string id || !enchantments.TryGetValue(id, out var cfg)) return;
        loading = true;
        normalTable.Checked = cfg.NormalTable;
        advancedTable.Checked = cfg.AdvancedTable;
        loot.Checked = cfg.Loot;
        SetRarity(cfg.Rarity);
        anvilCap.Value = Math.Clamp(cfg.AnvilMaxLevel, 0, 255);
        tableCap.Value = Math.Clamp(cfg.EnchantingTableMaxLevel, 0, 255);
        supportedItems.Text = string.Join(Environment.NewLine, cfg.SupportedItems);
        incompatible.Text = string.Join(Environment.NewLine, cfg.IncompatibleEnchantments);
        loading = false;
        RefreshIncompatiblePicker();
    }

    private void SetRarity(int value)
    {
        RarityOption selected = RarityOptions.OrderBy(option => Math.Abs(option.Value - value)).First();
        rarity.SelectedItem = selected;
    }

    private ItemChoice? SelectedItemChoice()
    {
        return itemList.SelectedItems.Count == 0 ? null : itemList.SelectedItems[0].Tag as ItemChoice;
    }

    private void AddSelectedItem()
    {
        ItemChoice? choice = SelectedItemChoice();
        if (choice != null) AddSupportedEntry(choice.Value);
    }

    private void AddSupportedEntry(string entry)
    {
        var values = ParseList(supportedItems.Text);
        if (!values.Contains(entry, StringComparer.OrdinalIgnoreCase))
        {
            values.Add(entry);
            supportedItems.Text = string.Join(Environment.NewLine, values);
        }
    }

    private void AddSelectedIncompatible()
    {
        if (incompatiblePicker.SelectedItem is string id)
        {
            var values = ParseList(incompatible.Text);
            if (!values.Contains(id, StringComparer.OrdinalIgnoreCase))
            {
                values.Add(id);
                incompatible.Text = string.Join(Environment.NewLine, values);
            }
        }
    }

    private void SaveSelectedFromUi()
    {
        if (loading || enchantmentList.SelectedItem is not string id) return;
        var cfg = enchantments[id];
        cfg.NormalTable = normalTable.Checked;
        cfg.AdvancedTable = advancedTable.Checked;
        cfg.Loot = loot.Checked;
        cfg.Rarity = rarity.SelectedItem is RarityOption option ? option.Value : 100;
        cfg.AnvilMaxLevel = (int)anvilCap.Value;
        cfg.EnchantingTableMaxLevel = (int)tableCap.Value;
        cfg.SupportedItems = ParseList(supportedItems.Text);
        cfg.IncompatibleEnchantments = ParseList(incompatible.Text);
        RefreshIncompatiblePicker();
    }

    private void ApplyGroupToCheckedEnchantments()
    {
        if (groupPicker.SelectedItem is not ItemChoice group) return;
        foreach (string id in CheckedGroupIds())
        {
            var cfg = enchantments[id];
            if (!cfg.SupportedItems.Contains(group.Value, StringComparer.OrdinalIgnoreCase)) cfg.SupportedItems.Add(group.Value);
            cfg.NormalTable = groupNormalTable.Checked;
            cfg.AdvancedTable = groupAdvancedTable.Checked;
            cfg.Loot = groupLoot.Checked;
            if (groupRarity.SelectedItem is RarityOption option) cfg.Rarity = option.Value;
        }
        SaveFile();
        LoadSelected();
    }

    private void RemoveGroupFromCheckedEnchantments()
    {
        if (groupPicker.SelectedItem is not ItemChoice group) return;
        foreach (string id in CheckedGroupIds())
        {
            enchantments[id].SupportedItems.RemoveAll(x => x.Equals(group.Value, StringComparison.OrdinalIgnoreCase));
        }
        SaveFile();
        LoadSelected();
    }

    private IEnumerable<string> CheckedGroupIds()
    {
        return groupEnchantments.CheckedItems.Cast<string>().ToList();
    }

    private void SetAllGroupChecks(bool value)
    {
        for (int i = 0; i < groupEnchantments.Items.Count; i++)
        {
            groupEnchantments.SetItemChecked(i, value);
        }
    }

    private void SaveFile()
    {
        SaveSelectedFromUi();
        var root = new JsonObject { ["configVersion"] = 2 };
        var entries = new JsonObject();
        foreach (var kv in enchantments.OrderBy(x => x.Key))
        {
            entries[kv.Key] = kv.Value.ToJson();
        }
        root["enchantments"] = entries;
        File.WriteAllText(configPath, root.ToJsonString(new JsonSerializerOptions { WriteIndented = true }));
        Text = "Eldritch Surge Config - salvato";
    }

    private static List<string> ParseList(string text) => text
        .Split(new[] { ',', ';', '\r', '\n' }, StringSplitOptions.RemoveEmptyEntries | StringSplitOptions.TrimEntries)
        .Distinct(StringComparer.OrdinalIgnoreCase)
        .ToList();

    private static string FindProjectRoot()
    {
        var dir = new DirectoryInfo(AppContext.BaseDirectory);
        while (dir != null)
        {
            if (File.Exists(Path.Combine(dir.FullName, "gradlew.bat")) && Directory.Exists(Path.Combine(dir.FullName, "src")))
            {
                return dir.FullName;
            }
            dir = dir.Parent;
        }
        return Directory.GetCurrentDirectory();
    }
}

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
        AdvancedTable = id.StartsWith("eldritch-surge:", StringComparison.OrdinalIgnoreCase),
        Loot = !id.StartsWith("minecraft:", StringComparison.OrdinalIgnoreCase) && !id.StartsWith("eldritch-surge:", StringComparison.OrdinalIgnoreCase)
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
