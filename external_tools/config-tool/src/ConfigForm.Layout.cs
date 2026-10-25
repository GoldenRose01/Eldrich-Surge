using System.Text.RegularExpressions;

namespace EldritchConfigTool;

internal sealed partial class ConfigForm
{
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

        enchantmentCategoryTabs.TabPages.Clear();
        enchantmentLists.Clear();
        foreach ((EnchantCategory category, string title) in CategoryPages())
        {
            var page = new TabPage(title);
            var list = CreateEnchantmentList(category);
            page.Controls.Add(list);
            enchantmentCategoryTabs.TabPages.Add(page);
        }
        panel.Controls.Add(enchantmentCategoryTabs, 0, 1);

        var pathButton = new Button { Text = "Mostra file config", Dock = DockStyle.Fill };
        pathButton.Click += (_, _) => MessageBox.Show(configPath, "File config");
        panel.Controls.Add(pathButton, 0, 2);
        return panel;
    }

    private ListBox CreateEnchantmentList(EnchantCategory category)
    {
        var list = new ListBox { Dock = DockStyle.Fill, IntegralHeight = false };
        list.SelectedIndexChanged += (_, _) => SelectEnchantmentFromCategory(list);
        enchantmentLists[category] = list;
        return list;
    }

    private void SelectEnchantmentFromCategory(ListBox source)
    {
        if (switchingCategorySelection || source.SelectedItem is not string id) return;

        switchingCategorySelection = true;
        foreach (ListBox list in enchantmentLists.Values)
        {
            if (!ReferenceEquals(list, source)) list.ClearSelected();
        }
        switchingCategorySelection = false;

        LoadSelected(id);
    }

    private string? SelectedEnchantmentId()
    {
        foreach (ListBox list in enchantmentLists.Values)
        {
            if (list.SelectedItem is string id) return id;
        }
        return null;
    }

    private bool SelectEnchantmentInCategories(string id)
    {
        foreach ((EnchantCategory category, _) in CategoryPages())
        {
            ListBox list = enchantmentLists[category];
            int index = list.Items.IndexOf(id);
            if (index < 0) continue;

            switchingCategorySelection = true;
            foreach (ListBox other in enchantmentLists.Values) other.ClearSelected();
            enchantmentCategoryTabs.SelectedIndex = CategoryPages().FindIndex(page => page.Category == category);
            list.SelectedIndex = index;
            switchingCategorySelection = false;
            LoadSelected(id);
            return true;
        }

        return false;
    }

    private void SelectFirstVisibleEnchantment()
    {
        foreach ((EnchantCategory category, _) in CategoryPages())
        {
            ListBox list = enchantmentLists[category];
            if (list.Items.Count == 0) continue;
            SelectEnchantmentInCategories((string)list.Items[0]);
            return;
        }
    }

    private static List<(EnchantCategory Category, string Title)> CategoryPages() => new()
    {
        (EnchantCategory.Weapons, "Armi"),
        (EnchantCategory.Defense, "Difese"),
        (EnchantCategory.Archery, "Arch"),
        (EnchantCategory.Tools, "Tool"),
        (EnchantCategory.Misc, "Misc")
    };

    private static EnchantCategory CategoryForEnchantment(string id, EnchantmentConfig cfg)
    {
        string key = CanonicalName(id.Split(':', 2).Last());
        List<string> supported = cfg.SupportedItems.Count > 0 ? cfg.SupportedItems : cfg.DatapackSupportedItems;
        string context = string.Join(" ", supported).ToLowerInvariant();

        if (NameMatches(key, ArcheryEnchantments) || ContextHasAny(context, "bow", "crossbow", "projectile"))
        {
            return EnchantCategory.Archery;
        }

        if (NameMatches(key, DefenseEnchantments) ||
            ContextHasAny(context, "armor", "head_armor", "chest_armor", "leg_armor", "foot_armor", "helmet", "chestplate", "leggings", "boots", "elytra", "shield", "equippable"))
        {
            return EnchantCategory.Defense;
        }

        if (NameMatches(key, ToolEnchantments) ||
            ContextHasAny(context, "pickaxe", "pickaxes", "shovel", "shovels", "hoe", "hoes", "shears", "mining", "fishing"))
        {
            return EnchantCategory.Tools;
        }

        if (NameMatches(key, WeaponEnchantments) ||
            ContextHasAny(context, "sword", "swords", "trident", "mace", "weapon", "melee"))
        {
            return EnchantCategory.Weapons;
        }

        return EnchantCategory.Misc;
    }

    private static bool NameMatches(string canonicalId, IEnumerable<string> names)
    {
        return names.Any(name => canonicalId.Contains(CanonicalName(name), StringComparison.OrdinalIgnoreCase));
    }

    private static bool ContextHasAny(string context, params string[] tokens)
    {
        return tokens.Any(token => context.Contains(token, StringComparison.OrdinalIgnoreCase));
    }

    private static string CanonicalName(string value)
    {
        return Regex.Replace(value.ToLowerInvariant(), @"[^a-z0-9]+", "");
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
        reload.Click += (_, _) => { LoadConfig(); RefreshEnchantmentList(); RefreshIncompatiblePicker(); RefreshGroupSummary(); };
        buttons.Controls.Add(save);
        buttons.Controls.Add(reload);
        return buttons;
    }

    private TabPage BuildPerGroupTab()
    {
        var page = new TabPage("Per tool/gruppo");
        var root = new TableLayoutPanel { Dock = DockStyle.Fill, ColumnCount = 2, RowCount = 5, Padding = new Padding(12) };
        root.ColumnStyles.Add(new ColumnStyle(SizeType.Absolute, 380));
        root.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100));
        root.RowStyles.Add(new RowStyle(SizeType.Absolute, 76));
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
        groupPicker.SelectedIndexChanged += (_, _) => RefreshGroupSummary();
        root.Controls.Add(Wrap("Tool o gruppo", groupPicker), 0, 0);

        groupRarity.Width = 310;
        groupRarity.SelectedIndex = 3;
        root.Controls.Add(Wrap("Rarita da applicare", groupRarity), 1, 0);

        groupTarget.Items.AddRange(new object[]
        {
            "Tutti gli incantesimi",
            "Solo minecraft/vanilla",
            "Solo Eldritch Surge/advanced",
            "Solo loot/altre mod"
        });
        groupTarget.SelectedIndex = 0;
        groupTarget.SelectedIndexChanged += (_, _) => RefreshGroupSummary();
        root.Controls.Add(Wrap("Ambito", groupTarget), 0, 1);

        var flags = new FlowLayoutPanel { Dock = DockStyle.Fill, WrapContents = false };
        flags.Controls.AddRange(new Control[] { groupNormalTable, groupAdvancedTable, groupLoot });
        root.Controls.Add(Wrap("Disponibilita da applicare", flags), 1, 1);

        var summaryPanel = new Panel { Dock = DockStyle.Fill };
        groupSummary.Dock = DockStyle.Top;
        groupSummary.Font = new Font(groupSummary.Font.FontFamily, 11, FontStyle.Regular);
        summaryPanel.Controls.Add(groupSummary);
        root.Controls.Add(Wrap("Configurazione del tool selezionato", summaryPanel), 0, 2);
        root.SetColumnSpan(root.GetControlFromPosition(0, 2)!, 2);

        var buttons = new FlowLayoutPanel { Dock = DockStyle.Fill, FlowDirection = FlowDirection.RightToLeft, WrapContents = false };
        var applyGroup = new Button { Text = "Applica gruppo", Width = 150, Height = 34 };
        applyGroup.Click += (_, _) => ApplyGroupToCheckedEnchantments();
        var removeGroup = new Button { Text = "Rimuovi gruppo", Width = 150, Height = 34 };
        removeGroup.Click += (_, _) => RemoveGroupFromCheckedEnchantments();
        buttons.Controls.Add(applyGroup);
        buttons.Controls.Add(removeGroup);
        root.Controls.Add(buttons, 0, 4);
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

}
