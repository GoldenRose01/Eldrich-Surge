package eldritch.surge.enchantment.mechanics;

import eldritch.surge.EldritchSurge;
import eldritch.surge.config.EnchantmentCapsConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Optional bridge to EnchLib; both mods keep their own runtime and JAR. */
public final class EnchLibIntegration {
    private EnchLibIntegration() {
    }

    public static void synchronizeEnchantments() {
        if (!FabricLoader.getInstance().isModLoaded("enchlib")) {
            EldritchSurge.LOGGER.info("EnchLib not installed; skipping optional enchantment sync.");
            return;
        }

        Map<String, Integer> maxLevels = new TreeMap<>();
        Map<String, List<String>> categories = new TreeMap<>();
        Map<String, List<String>> mobCategories = new TreeMap<>();
        for (ModEnchantmentDefinition definition : ModEnchantmentDefinitions.ALL) {
            String id = EldritchSurge.MOD_ID + ":" + definition.id();
            maxLevels.put(id, definition.maxLevel());
            categories.put(id, definition.supportedItems());
        }

        mobCategories.put("eldritch-surge:bane_of_end", List.of("void"));
        mobCategories.put("eldritch-surge:blade_of_apocalypse", List.of("animals", "undead", "void", "hell", "water"));
        mobCategories.put("eldritch-surge:butcher", List.of("animals"));
        mobCategories.put("eldritch-surge:creeping_threat", List.of("arthropods"));
        mobCategories.put("eldritch-surge:exorcist", List.of("hell"));
        mobCategories.put("eldritch-surge:flogging", List.of("rebel"));
        mobCategories.put("eldritch-surge:herbicide", List.of("fungi"));
        mobCategories.put("eldritch-surge:katana", List.of("all"));
        mobCategories.put("eldritch-surge:smoother", List.of("cubic"));
        mobCategories.put("eldritch-surge:undead_slayer", List.of("undead"));
        mobCategories.put("eldritch-surge:witch_hunter", List.of("magik"));
        mobCategories.put("eldritch-surge:wrath_of_the_abyss", List.of("water"));

        // Extend vanilla Smite to EnchLib's undead category and expose the target category there too.
        maxLevels.put("minecraft:smite", 5);
        categories.put("minecraft:smite", List.of("#minecraft:enchantable/weapon"));
        mobCategories.put("minecraft:smite", List.of("undead"));

        Map<String, Boolean> normalDefaults = new TreeMap<>();
        Map<String, Boolean> advancedDefaults = new TreeMap<>();
        Map<String, Integer> chanceDefaults = new TreeMap<>();
        for (ModEnchantmentDefinition definition : ModEnchantmentDefinitions.ALL) {
            String id = EldritchSurge.MOD_ID + ":" + definition.id();
            normalDefaults.put(id, EnchantmentCapsConfig.isAllowedInNormalTable(net.minecraft.resources.Identifier.tryParse(id)));
            advancedDefaults.put(id, EnchantmentCapsConfig.isAllowedInAdvancedTable(net.minecraft.resources.Identifier.tryParse(id)));
            chanceDefaults.put(id, 100);
        }
        loadVanillaPresets(normalDefaults, advancedDefaults, chanceDefaults);

        try {
            Class<?> api = Class.forName("goldenrose01.enchlib.api.EnchantLibAPI");
            api.getMethod("registerEnchantingTable", String.class, String.class)
                    .invoke(null, "minecraft:enchanting_table", "Enchanting Table");
            api.getMethod("registerEnchantingTable", String.class, String.class)
                    .invoke(null, "eldritch-surge:advanced_enchanting_table", "Advanced Enchanting Table");
            var registerDefaults = api.getMethod("registerEnchantingTableDefaults", String.class, String.class, Map.class, Map.class);
            registerDefaults.invoke(null, "minecraft:enchanting_table", "Enchanting Table", normalDefaults, chanceDefaults);
            registerDefaults.invoke(null, "eldritch-surge:advanced_enchanting_table", "Advanced Enchanting Table", advancedDefaults, chanceDefaults);
            try {
                Method register = api.getMethod("registerEnchantments", Map.class, Map.class, Map.class);
                Object count = register.invoke(null, maxLevels, categories, mobCategories);
                EldritchSurge.LOGGER.info("Registered {} enchantment entries with EnchLib.", count);
            } catch (NoSuchMethodException olderApi) {
                Method register = api.getMethod("registerEnchantments", Map.class, Map.class);
                Object count = register.invoke(null, maxLevels, categories);
                EldritchSurge.LOGGER.warn("EnchLib API is outdated; target mob categories were not synchronized ({} entries).", count);
            }
        } catch (ReflectiveOperationException | LinkageError exception) {
            EldritchSurge.LOGGER.error("EnchLib was found but its enchantment API could not be called.", exception);
        }
    }

    private static void loadVanillaPresets(Map<String, Boolean> normal, Map<String, Boolean> advanced, Map<String, Integer> chances) {
        try (var input = EnchLibIntegration.class.getClassLoader().getResourceAsStream("config/AviableEnch.config")) {
            if (input == null) return;
            for (String line : new String(input.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8).split("\\R")) {
                String trimmed = line.trim(); int separator = trimmed.indexOf('=');
                if (separator <= 0 || trimmed.startsWith("#")) continue;
                String id = trimmed.substring(0, separator).trim();
                if (!id.startsWith("minecraft:")) continue;
                boolean replaced = id.equals("minecraft:sharpness") || id.equals("minecraft:smite") || id.equals("minecraft:bane_of_arthropods");
                normal.put(id, !replaced);
                advanced.put(id, false);
                chances.put(id, 100);
            }
        } catch (Exception exception) {
            EldritchSurge.LOGGER.warn("Could not load EnchLib's vanilla enchantment presets.", exception);
        }
    }

    /** Seeds editable EnchLib table settings from Eldritch's effective presets without replacing user edits. */
    public static void synchronizeTablePresets() {
        if (!FabricLoader.getInstance().isModLoaded("enchlib")) return;
        Map<String, Boolean> normal = new TreeMap<>();
        Map<String, Boolean> advanced = new TreeMap<>();
        Map<String, Integer> chances = new TreeMap<>();
        EnchantmentCapsConfig.enchantmentOverrides().forEach((id, value) -> {
            normal.put(id, EnchantmentCapsConfig.isAllowedInNormalTable(net.minecraft.resources.Identifier.tryParse(id)));
            advanced.put(id, EnchantmentCapsConfig.isAllowedInAdvancedTable(net.minecraft.resources.Identifier.tryParse(id)));
            chances.put(id, value.rarity);
        });
        try {
            Class<?> api = Class.forName("goldenrose01.enchlib.api.EnchantLibAPI");
            var register = api.getMethod("registerEnchantingTableDefaults", String.class, String.class, Map.class, Map.class);
            register.invoke(null, "minecraft:enchanting_table", "Enchanting Table", normal, chances);
            register.invoke(null, "eldritch-surge:advanced_enchanting_table", "Advanced Enchanting Table", advanced, chances);
        } catch (ReflectiveOperationException | LinkageError exception) {
            EldritchSurge.LOGGER.error("Could not synchronize EnchLib table presets.", exception);
        }
    }
}
