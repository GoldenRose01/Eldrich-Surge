package eldritch.surge.enchantment.mechanics;

import eldritch.surge.EldritchSurge;
import eldritch.surge.config.EnchantmentCapsConfig;
import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

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
        Map<String, String> rarities = new TreeMap<>();
        Map<String, List<String>> exclusiveSets = new TreeMap<>();
        for (ModEnchantmentDefinition definition : ModEnchantmentDefinitions.ALL) {
            String id = EldritchSurge.MOD_ID + ":" + definition.id();
            maxLevels.put(id, definition.maxLevel());
            categories.put(id, definition.supportedItems());
            rarities.put(id, toEnchLibRarity(definition.classification()));
            if (!definition.exclusiveSet().isBlank() && !definition.exclusiveSet().startsWith("#")) {
                exclusiveSets.computeIfAbsent(definition.exclusiveSet(), ignored -> new java.util.ArrayList<>()).add(id);
            }
        }

        mobCategories.putAll(MobCategoryDamageMechanics.enchantmentCategoryDefaults());

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
            try {
                Object count = api.getMethod("registerRarityDefaults", Map.class).invoke(null, rarities);
                EldritchSurge.LOGGER.info("Registered {} rarity presets with EnchLib.", count);
            } catch (NoSuchMethodException unsupported) {
                EldritchSurge.LOGGER.warn("EnchLib API does not expose rarity preset registration.");
            }
            synchronizeIncompatibilityDefaults(api, exclusiveSets);
            try {
                Object count = api.getMethod("registerMobCategories", Map.class)
                        .invoke(null, vanillaMobCategoryDefaults());
                EldritchSurge.LOGGER.info("Registered {} default mob assignments with EnchLib.", count);
            } catch (NoSuchMethodException unsupported) {
                EldritchSurge.LOGGER.warn("EnchLib API does not expose mod-provided mob category defaults.");
            }
        } catch (ReflectiveOperationException | LinkageError exception) {
            EldritchSurge.LOGGER.error("EnchLib was found but its enchantment API could not be called.", exception);
        }
    }

    /** Transfers the vanilla entity-tag catalog as reset defaults; EnchLib world edits take precedence. */
    private static Map<String, List<String>> vanillaMobCategoryDefaults() {
        Map<String, List<String>> result = new TreeMap<>();
        for (String category : List.of("animals", "arthropods", "cubic", "fungi", "hell", "magik",
                "rebel", "undead", "void", "water")) {
            String resource = "data/eldritch-surge/tags/entity_type/" + category + ".json";
            try (var input = EnchLibIntegration.class.getClassLoader().getResourceAsStream(resource)) {
                if (input == null) continue;
                var root = JsonParser.parseReader(new java.io.InputStreamReader(input, java.nio.charset.StandardCharsets.UTF_8));
                JsonElement values = root.getAsJsonObject().get("values");
                if (values == null || !values.isJsonArray()) continue;
                for (JsonElement value : values.getAsJsonArray()) {
                    String id = value.isJsonPrimitive() ? value.getAsString()
                            : value.getAsJsonObject().has("id") ? value.getAsJsonObject().get("id").getAsString() : "";
                    if (!id.isBlank() && !id.startsWith("#")) {
                        result.computeIfAbsent(id, ignored -> new ArrayList<>()).add(category);
                    }
                }
            } catch (Exception exception) {
                EldritchSurge.LOGGER.warn("Could not read default entity category tag {}.", category, exception);
            }
        }
        result.replaceAll((id, categories) -> categories.stream().distinct().sorted().toList());
        return Map.copyOf(result);
    }

    private static String toEnchLibRarity(String classification) {
        return switch (classification.toLowerCase(java.util.Locale.ROOT)) {
            case "common" -> "common";
            case "uncommon" -> "uncommon";
            case "rare", "epic" -> "rare";
            default -> "very_rare";
        };
    }

    /** Seeds only empty EnchLib incompatibility lists; existing player edits always win. */
    private static void synchronizeIncompatibilityDefaults(Class<?> api, Map<String, List<String>> groups) {
        try {
            Method getter = api.getMethod("getIncompatibleEnchantments", String.class);
            Method setter = api.getMethod("setIncompatibleEnchantments", String.class, List.class);
            Map<String, java.util.Set<String>> defaults = new TreeMap<>();
            for (List<String> members : groups.values()) {
                for (String id : members) {
                    defaults.computeIfAbsent(id, ignored -> new java.util.TreeSet<>())
                            .addAll(members.stream().filter(other -> !other.equals(id)).toList());
                }
            }
            addIncompatibility(defaults, "minecraft:sharpness", "eldritch-surge:katana");
            addIncompatibility(defaults, "minecraft:bane_of_arthropods", "eldritch-surge:creeping_threat");
            addIncompatibility(defaults, "eldritch-surge:star_fate", "eldritch-surge:witch_hunter",
                    "eldritch-surge:creeping_threat", "eldritch-surge:flogging", "eldritch-surge:herbicide",
                    "eldritch-surge:smoother");
            addIncompatibility(defaults, "eldritch-surge:blade_of_apocalypse", "eldritch-surge:butcher",
                    "eldritch-surge:undead_slayer", "eldritch-surge:bane_of_end", "eldritch-surge:exorcist",
                    "eldritch-surge:wrath_of_the_abyss");
            addIncompatibility(defaults, "eldritch-surge:gream_reaper", "eldritch-surge:star_fate",
                    "eldritch-surge:blade_of_apocalypse", "eldritch-surge:katana");
            for (Map.Entry<String, java.util.Set<String>> entry : defaults.entrySet()) {
                Object configured = getter.invoke(null, entry.getKey());
                if (configured instanceof List<?> current && current.isEmpty()) {
                    setter.invoke(null, entry.getKey(), List.copyOf(entry.getValue()));
                }
            }
        } catch (NoSuchMethodException unsupported) {
            EldritchSurge.LOGGER.warn("EnchLib API does not expose incompatibility defaults.");
        } catch (ReflectiveOperationException | LinkageError exception) {
            EldritchSurge.LOGGER.warn("Could not synchronize EnchLib incompatibility defaults.", exception);
        }
    }

    private static void addIncompatibility(Map<String, java.util.Set<String>> defaults, String id, String... others) {
        for (String other : others) {
            defaults.computeIfAbsent(id, ignored -> new java.util.TreeSet<>()).add(other);
            defaults.computeIfAbsent(other, ignored -> new java.util.TreeSet<>()).add(id);
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
