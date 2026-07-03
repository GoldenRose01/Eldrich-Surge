package eldritch.surge.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eldritch.surge.EldritchSurge;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.core.component.DataComponents;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public final class EnchantmentCapsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve(EldritchSurge.MOD_ID)
            .resolve("enchantment_caps.json");
    private static final Set<String> NORMAL_TABLE_DEFAULTS = Set.of(
            "armored",
            "hicker",
            "ice_speed",
            "soil_falling",
            "weapon_protection",
            "backslash",
            "bane_of_end",
            "butcher",
            "creeping_threat",
            "dash",
            "exorcist",
            "flogging",
            "freeze_aspect",
            "gream_reaper",
            "herbicide",
            "inking",
            "katana",
            "leeching_aspect",
            "midas_touch",
            "sea_breeze",
            "smoother",
            "superweight",
            "undead_slayer",
            "witch_hunter",
            "wrath_of_the_abyss",
            "curse_of_target",
            "piercing",
            "pop",
            "replenish",
            "sniper",
            "digger",
            "pruning",
            "sickened_of_hell",
            "smithcrafts",
            "refill",
            "siphon",
            "vacuum",
            "curse_of_fragility"
    );
    private static final Set<String> LOOT_DEFAULTS = Set.of(
            "gream_reaper",
            "soft_falling",
            "cocktail_spell",
            "ragnarok",
            "red_moon",
            "storm_spell",
            "trench_spell"
    );

    private static Data data = new Data();

    private EnchantmentCapsConfig() {
    }

    public static void loadAndSyncWithRegistry(Collection<Identifier> knownEnchantments) {
        data = readOrCreate();
        boolean needsAvailabilityMigration = data.configVersion < 2;

        for (Identifier id : knownEnchantments) {
            CapsOverride override = data.enchantments.computeIfAbsent(id.toString(), ignored -> defaultOverride(id));
            if (needsAvailabilityMigration) {
                copyAvailability(defaultOverride(id), override);
            }
        }

        data.configVersion = 2;
        save();
    }

    public static int resolveAnvilCap(Identifier enchantmentId, int vanillaCap) {
        return resolve(enchantmentId, vanillaCap, CapKind.ANVIL);
    }

    public static int resolveEnchantingTableCap(Identifier enchantmentId, int vanillaCap) {
        return resolve(enchantmentId, vanillaCap, CapKind.ENCHANTING_TABLE);
    }

    public static Map<String, CapsOverride> enchantmentOverrides() {
        return Map.copyOf(data.enchantments);
    }

    public static int getAnvilCap(String enchantmentId) {
        return data.enchantments.computeIfAbsent(enchantmentId, ignored -> new CapsOverride()).anvilMaxLevel;
    }

    public static void setAnvilCap(String enchantmentId, int maxLevel) {
        data.enchantments.computeIfAbsent(enchantmentId, ignored -> new CapsOverride()).anvilMaxLevel = Math.max(0, maxLevel);
    }

    public static int getEnchantingTableCap(String enchantmentId) {
        return data.enchantments.computeIfAbsent(enchantmentId, ignored -> new CapsOverride()).enchantingTableMaxLevel;
    }

    public static void setEnchantingTableCap(String enchantmentId, int maxLevel) {
        data.enchantments.computeIfAbsent(enchantmentId, ignored -> new CapsOverride()).enchantingTableMaxLevel = Math.max(0, maxLevel);
    }

    public static boolean isAllowedInNormalTable(Identifier enchantmentId) {
        return data.enchantments.computeIfAbsent(enchantmentId.toString(), ignored -> defaultOverride(enchantmentId)).normalTable;
    }

    public static void setAllowedInNormalTable(String enchantmentId, boolean allowed) {
        data.enchantments.computeIfAbsent(enchantmentId, ignored -> defaultOverride(Identifier.tryParse(enchantmentId))).normalTable = allowed;
    }

    public static boolean isAllowedInAdvancedTable(Identifier enchantmentId) {
        return data.enchantments.computeIfAbsent(enchantmentId.toString(), ignored -> defaultOverride(enchantmentId)).advancedTable;
    }

    public static void setAllowedInAdvancedTable(String enchantmentId, boolean allowed) {
        data.enchantments.computeIfAbsent(enchantmentId, ignored -> defaultOverride(Identifier.tryParse(enchantmentId))).advancedTable = allowed;
    }

    public static boolean isAllowedAsLoot(Identifier enchantmentId) {
        return data.enchantments.computeIfAbsent(enchantmentId.toString(), ignored -> defaultOverride(enchantmentId)).loot;
    }

    public static void setAllowedAsLoot(String enchantmentId, boolean allowed) {
        data.enchantments.computeIfAbsent(enchantmentId, ignored -> defaultOverride(Identifier.tryParse(enchantmentId))).loot = allowed;
    }

    public static boolean isAllowedForItem(Identifier enchantmentId, ItemStack stack) {
        CapsOverride override = data.enchantments.computeIfAbsent(enchantmentId.toString(), ignored -> defaultOverride(enchantmentId));
        if (override.supportedItems.isEmpty()) {
            return true;
        }

        Identifier itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        for (String entry : override.supportedItems) {
            if (entry.startsWith("#")) {
                Identifier tagId = Identifier.tryParse(entry.substring(1));
                if (tagId != null && stack.is(TagKey.create(Registries.ITEM, tagId))) {
                    return true;
                }
            } else if (itemId.toString().equals(entry)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isCompatibleWithExistingEnchantments(Identifier enchantmentId, ItemStack stack) {
        CapsOverride override = data.enchantments.computeIfAbsent(enchantmentId.toString(), ignored -> defaultOverride(enchantmentId));
        Set<String> blocked = new HashSet<>(override.incompatibleEnchantments);
        if (blocked.isEmpty()) {
            return true;
        }

        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        for (var entry : enchantments.entrySet()) {
            Identifier existing = entry.getKey().unwrapKey()
                    .map(key -> key.identifier())
                    .orElse(null);
            if (existing != null && blocked.contains(existing.toString())) {
                return false;
            }
        }

        return true;
    }

    public static boolean passesRarity(Identifier enchantmentId, ItemStack stack, int slot, int level) {
        CapsOverride override = data.enchantments.computeIfAbsent(enchantmentId.toString(), ignored -> defaultOverride(enchantmentId));
        int rarity = Math.max(0, Math.min(100, override.rarity));
        if (rarity >= 100) {
            return true;
        }
        if (rarity <= 0) {
            return false;
        }

        int roll = Math.floorMod((enchantmentId + "|" + BuiltInRegistries.ITEM.getKey(stack.getItem()) + "|" + slot + "|" + level).hashCode(), 100);
        return roll < rarity;
    }

    private static int resolve(Identifier enchantmentId, int vanillaCap, CapKind kind) {
        CapsOverride override = data.enchantments.get(enchantmentId.toString());
        if (override == null) {
            return vanillaCap;
        }

        int configured = switch (kind) {
            case ANVIL -> override.anvilMaxLevel;
            case ENCHANTING_TABLE -> override.enchantingTableMaxLevel;
        };

        return configured > 0 ? configured : vanillaCap;
    }

    private static Data readOrCreate() {
        if (!Files.exists(CONFIG_PATH)) {
            return new Data();
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            Data loaded = GSON.fromJson(reader, Data.class);
            return loaded == null ? new Data() : loaded;
        } catch (IOException | RuntimeException exception) {
            EldritchSurge.LOGGER.warn("Could not read {}, using defaults.", CONFIG_PATH, exception);
            return new Data();
        }
    }

    private static CapsOverride defaultOverride(Identifier enchantmentId) {
        CapsOverride override = new CapsOverride();
        if (enchantmentId == null) {
            override.loot = true;
            return override;
        }

        if (enchantmentId.getNamespace().equals("minecraft")) {
            override.normalTable = true;
        } else if (enchantmentId.getNamespace().equals(EldritchSurge.MOD_ID)) {
            String path = enchantmentId.getPath();
            override.normalTable = NORMAL_TABLE_DEFAULTS.contains(path);
            override.advancedTable = !override.normalTable;
            override.loot = LOOT_DEFAULTS.contains(path);
        } else {
            override.loot = true;
        }

        return override;
    }

    private static void copyAvailability(CapsOverride source, CapsOverride target) {
        target.normalTable = source.normalTable;
        target.advancedTable = source.advancedTable;
        target.loot = source.loot;
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException exception) {
            EldritchSurge.LOGGER.error("Could not save {}.", CONFIG_PATH, exception);
        }
    }

    private enum CapKind {
        ANVIL,
        ENCHANTING_TABLE
    }

    public static final class Data {
        public int configVersion = 2;
        public Map<String, CapsOverride> enchantments = new TreeMap<>();
    }

    public static final class CapsOverride {
        public int anvilMaxLevel = 0;
        public int enchantingTableMaxLevel = 0;
        public boolean normalTable = false;
        public boolean advancedTable = false;
        public boolean loot = false;
        public int rarity = 100;
        public List<String> supportedItems = new ArrayList<>();
        public List<String> incompatibleEnchantments = new ArrayList<>();
    }
}
