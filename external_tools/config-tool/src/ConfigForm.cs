using System.Text.Json;
using System.Text.Json.Nodes;

namespace EldritchConfigTool;

internal sealed partial class ConfigForm : Form
{
    private readonly string rootPath;
    private readonly string configPath;
    private readonly string iconPath;
    private readonly Dictionary<string, EnchantmentConfig> enchantments = new(StringComparer.OrdinalIgnoreCase);
    private readonly List<ItemChoice> itemChoices = new();
    private readonly ImageList itemImages = new() { ImageSize = new Size(24, 24), ColorDepth = ColorDepth.Depth32Bit };

    private readonly TabControl enchantmentCategoryTabs = new() { Dock = DockStyle.Fill };
    private readonly Dictionary<EnchantCategory, ListBox> enchantmentLists = new();
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
    private readonly ComboBox groupTarget = new() { DropDownStyle = ComboBoxStyle.DropDownList };
    private readonly ComboBox groupRarity = new() { DropDownStyle = ComboBoxStyle.DropDownList };
    private readonly CheckBox groupNormalTable = new() { Text = "Tavolo normale", AutoSize = true };
    private readonly CheckBox groupAdvancedTable = new() { Text = "Advanced table", AutoSize = true };
    private readonly CheckBox groupLoot = new() { Text = "Loot", AutoSize = true };
    private readonly Label groupSummary = new() { AutoSize = true };

    private bool loading;
    private bool switchingCategorySelection;

    public ConfigForm()
    {
        Text = "Eldritch Surge Config";
        MinimumSize = new Size(1320, 820);
        StartPosition = FormStartPosition.CenterScreen;

        rootPath = ToolPaths.FindProjectRoot();
        configPath = ToolPaths.ConfigFile(rootPath);
        iconPath = ToolPaths.IconDirectory(rootPath);

        BuildUi();
        LoadConfig();
        LoadItems();
        RefreshEnchantmentList();
        RefreshItemList();
        RefreshIncompatiblePicker();
        RefreshGroupSummary();
    }

    private void RefreshEnchantmentList()
    {
        string previous = SelectedEnchantmentId() ?? "";
        string filter = enchantmentSearch.Text.Trim();

        switchingCategorySelection = true;
        foreach (ListBox list in enchantmentLists.Values)
        {
            list.BeginUpdate();
            list.Items.Clear();
        }

        foreach (string id in enchantments.Keys.OrderBy(x => x))
        {
            if (filter.Length > 0 && !id.Contains(filter, StringComparison.OrdinalIgnoreCase)) continue;
            EnchantCategory category = CategoryForEnchantment(id, enchantments[id]);
            enchantmentLists[category].Items.Add(id);
        }

        foreach (ListBox list in enchantmentLists.Values)
        {
            list.EndUpdate();
        }
        switchingCategorySelection = false;

        if (previous.Length > 0 && SelectEnchantmentInCategories(previous)) return;
        SelectFirstVisibleEnchantment();
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

    private void RefreshIncompatiblePicker()
    {
        string? selectedContext = SelectedItemChoice()?.Value ?? ParseList(supportedItems.Text).FirstOrDefault();
        string current = SelectedEnchantmentId() ?? "";

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
        string? id = SelectedEnchantmentId();
        if (id == null) return;
        LoadSelected(id);
    }

    private void LoadSelected(string id)
    {
        if (!enchantments.TryGetValue(id, out var cfg)) return;
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
        string? id = SelectedEnchantmentId();
        if (loading || id == null) return;
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
        foreach (string id in GroupTargetIds())
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
        RefreshGroupSummary();
    }

    private void RemoveGroupFromCheckedEnchantments()
    {
        if (groupPicker.SelectedItem is not ItemChoice group) return;
        foreach (string id in GroupTargetIds())
        {
            enchantments[id].SupportedItems.RemoveAll(x => x.Equals(group.Value, StringComparison.OrdinalIgnoreCase));
        }
        SaveFile();
        LoadSelected();
        RefreshGroupSummary();
    }

    private IEnumerable<string> GroupTargetIds()
    {
        int target = groupTarget.SelectedIndex;
        return enchantments.Keys
            .Where(id => target switch
            {
                1 => id.StartsWith("minecraft:", StringComparison.OrdinalIgnoreCase),
                2 => id.StartsWith("eldrich-surge:", StringComparison.OrdinalIgnoreCase),
                3 => !id.StartsWith("minecraft:", StringComparison.OrdinalIgnoreCase) && !id.StartsWith("eldrich-surge:", StringComparison.OrdinalIgnoreCase),
                _ => true
            })
            .OrderBy(id => id)
            .ToList();
    }

    private void RefreshGroupSummary()
    {
        if (groupPicker.SelectedItem is not ItemChoice group)
        {
            groupSummary.Text = "";
            return;
        }

        List<string> ids = GroupTargetIds().ToList();
        int alreadyConfigured = ids.Count(id => enchantments[id].SupportedItems.Contains(group.Value, StringComparer.OrdinalIgnoreCase));
        string targetName = groupTarget.SelectedItem?.ToString() ?? "Tutti gli incantesimi";
        groupSummary.Text =
            $"Tool/gruppo: {group.Label}{Environment.NewLine}" +
            $"Valore nel config: {group.Value}{Environment.NewLine}" +
            $"Ambito: {targetName}{Environment.NewLine}" +
            $"Incantesimi coinvolti dall'azione: {ids.Count}{Environment.NewLine}" +
            $"Gia configurati per questo tool/gruppo: {alreadyConfigured}{Environment.NewLine}{Environment.NewLine}" +
            "Applica gruppo aggiunge questo tool/gruppo agli incantesimi dell'ambito scelto e imposta disponibilita/rarita. " +
            "Rimuovi gruppo toglie solo questo tool/gruppo dagli stessi incantesimi.";
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

}
