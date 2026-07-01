package eldritch.surge.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eldritch.surge.EldritchSurge;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public final class EnchantmentCapsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve(EldritchSurge.MOD_ID)
            .resolve("enchantment_caps.json");

    private static Data data = new Data();

    private EnchantmentCapsConfig() {
    }

    public static void loadAndSyncWithRegistry(Collection<Identifier> knownEnchantments) {
        data = readOrCreate();

        for (Identifier id : knownEnchantments) {
            data.enchantments.putIfAbsent(id.toString(), new CapsOverride());
        }

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
        public int configVersion = 1;
        public Map<String, CapsOverride> enchantments = new TreeMap<>();
    }

    public static final class CapsOverride {
        public int anvilMaxLevel = 0;
        public int enchantingTableMaxLevel = 0;
    }
}
