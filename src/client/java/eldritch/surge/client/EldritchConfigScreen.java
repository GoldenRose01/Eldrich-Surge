package eldritch.surge.client;

import eldritch.surge.config.EnchantmentCapsConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/** Loaded only when the optional Cloth Config mod is installed. */
final class EldritchConfigScreen {
    private EldritchConfigScreen() {
    }

    static Screen create(Screen parent) {
        boolean editable = EnchantmentCapsConfig.canEditWorldRules();
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(text("title"))
                .setEditable(editable)
                .setShouldListSmoothScroll(true)
                .setShouldTabsSmoothScroll(true)
                .setSavingRunnable(() -> {
                    if (EnchantmentCapsConfig.canEditWorldRules()) EnchantmentCapsConfig.save();
                });
        ConfigEntryBuilder entries = builder.entryBuilder();
        Map<String, ConfigCategory> categories = new LinkedHashMap<>();
        categories.put("eldritch-surge", builder.getOrCreateCategory(text("category.eldritch")));
        categories.put("minecraft", builder.getOrCreateCategory(text("category.minecraft")));

        for (String enchantmentId : new TreeMap<>(EnchantmentCapsConfig.enchantmentOverrides()).keySet()) {
            Identifier id = Identifier.tryParse(enchantmentId);
            if (id == null) continue;
            ConfigCategory category = categories.computeIfAbsent(id.getNamespace(), namespace ->
                    builder.getOrCreateCategory(Component.literal(namespace)));
            SubCategoryBuilder group = entries.startSubCategory(
                            Component.translatable("enchantment." + enchantmentId.replace(':', '.')))
                    .setExpanded(false)
                    .setTooltip(Component.literal(enchantmentId), text("level_caps.group_description"));
            addToggle(group, entries, "normal_table", EnchantmentCapsConfig.isAllowedInNormalTable(id),
                    value -> EnchantmentCapsConfig.setAllowedInNormalTable(enchantmentId, value));
            addToggle(group, entries, "advanced_table", EnchantmentCapsConfig.isAllowedInAdvancedTable(id),
                    value -> EnchantmentCapsConfig.setAllowedInAdvancedTable(enchantmentId, value));
            addToggle(group, entries, "loot", EnchantmentCapsConfig.isAllowedAsLoot(id),
                    value -> EnchantmentCapsConfig.setAllowedAsLoot(enchantmentId, value));
            addLevel(group, entries, "anvil_max_level", EnchantmentCapsConfig.getAnvilCap(enchantmentId),
                    value -> EnchantmentCapsConfig.setAnvilCap(enchantmentId, value));
            addLevel(group, entries, "enchanting_table_max_level", EnchantmentCapsConfig.getEnchantingTableCap(enchantmentId),
                    value -> EnchantmentCapsConfig.setEnchantingTableCap(enchantmentId, value));
            category.addEntry(group.build());
        }
        for (ConfigCategory category : categories.values()) {
            category.addEntry(entries.startTextDescription(text(editable ? "world_editable" : "world_read_only")).build());
        }
        return builder.build();
    }

    private static void addToggle(SubCategoryBuilder group, ConfigEntryBuilder entries, String key,
                                  boolean value, Consumer<Boolean> save) {
        group.add(entries.startBooleanToggle(text(key), value)
                .setTooltip(text(key + ".description"))
                .setSaveConsumer(updated -> {
                    if (EnchantmentCapsConfig.canEditWorldRules()) save.accept(updated);
                }).build());
    }

    private static void addLevel(SubCategoryBuilder group, ConfigEntryBuilder entries, String key,
                                 int value, Consumer<Integer> save) {
        group.add(entries.startIntField(text(key), value)
                .setMin(0)
                .setTooltip(text("zero_uses_vanilla"))
                .setSaveConsumer(updated -> {
                    if (EnchantmentCapsConfig.canEditWorldRules()) save.accept(updated);
                }).build());
    }

    private static Component text(String key) {
        return Component.translatable("config.eldritch-surge." + key);
    }
}
