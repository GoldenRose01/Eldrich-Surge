package eldritch.surge.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eldritch.surge.EldritchSurge;
import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinitions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

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

    private static Data data = new Data();
    private static Path worldConfigPath;
    private static MinecraftServer activeServer;
    private static boolean clientWorldSnapshot;
    private static boolean clientCanEditWorld;

    private EnchantmentCapsConfig() {
    }

    /** Reads saved presets early so EnchLib's optional menu can seed table defaults before a world is opened. */
    public static void prepareForIntegration() {
        data = readGlobalOrCreate();
    }

    public static void loadAndSyncWithRegistry(Collection<Identifier> knownEnchantments) {
        data = readGlobalOrCreate();
        boolean needsAvailabilityMigration = data.configVersion < 2;
        boolean needsVanillaReplacementMigration = data.configVersion < 3;
        boolean needsBaseLevelPresetMigration = data.configVersion < 4;

        for (Identifier id : knownEnchantments) {
            CapsOverride override = data.enchantments.computeIfAbsent(id.toString(), ignored -> defaultOverride(id));
            if (needsAvailabilityMigration) {
                copyAvailability(defaultOverride(id), override);
            }
        }

        if (needsVanillaReplacementMigration) {
            disableReplacedVanillaEnchantments();
        }

        if (needsBaseLevelPresetMigration) {
            applyBaseLevelPresets();
        }

        data.configVersion = 4;
        saveGlobal();
    }

    /** Loads the saved world's overrides on top of the global defaults and persists any newly registered IDs. */
    public static void activateWorld(MinecraftServer server, Collection<Identifier> knownEnchantments) {
        activeServer = server;
        Path worldRoot = server.getWorldPath(LevelResource.ROOT);
        worldConfigPath = worldRoot.resolve("eldrich-surge").resolve("enchantment_caps.json");

        Data defaults = readGlobalOrCreate();
        boolean hasWorldConfig = Files.exists(worldConfigPath);
        Data world = readWorldOrDefaults(defaults);
        for (Identifier id : knownEnchantments) {
            world.enchantments.putIfAbsent(id.toString(), copyOverride(defaultOverride(id)));
        }
        world.configVersion = Math.max(world.configVersion, defaults.configVersion);
        data = world;
        if (!hasWorldConfig) captureEnchLibWorldValues();
        save();
    }

    /** Stops treating a previous save's settings as active after its server shuts down. */
    public static void deactivateWorld() {
        activeServer = null;
        worldConfigPath = null;
        clientWorldSnapshot = false;
        clientCanEditWorld = false;
        data = readGlobalOrCreate();
    }

    public static String serializeForSync() {
        return GSON.toJson(data);
    }

    /** Applies only server-sent settings on the client. Client state is never written to disk. */
    public static void applySynchronizedData(String json) {
        try {
            Data synchronizedData = GSON.fromJson(json, Data.class);
            if (synchronizedData != null && synchronizedData.enchantments != null) data = synchronizedData;
        } catch (RuntimeException exception) {
            EldritchSurge.LOGGER.warn("Received invalid world enchantment settings; keeping the current snapshot.", exception);
        }
    }

    public static void applyClientWorldSnapshot(String json, boolean canEdit) {
        applySynchronizedData(json);
        clientWorldSnapshot = true;
        clientCanEditWorld = canEdit;
    }

    public static boolean canEditWorldRules() {
        return clientWorldSnapshot && clientCanEditWorld;
    }

    public static void clearClientWorldSnapshot() {
        clientWorldSnapshot = false;
        clientCanEditWorld = false;
        data = readGlobalOrCreate();
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
        List<String> rules = supportedItemRules(enchantmentId);
        if (rules.isEmpty()) {
            return true;
        }
        return matchesEnchLibRules(rules, stack);
    }

    /** Effective compatibility rules, including the active world's EnchLib overrides. */
    public static List<String> supportedItemRules(Identifier enchantmentId) {
        CapsOverride override = data.enchantments.computeIfAbsent(enchantmentId.toString(), ignored -> defaultOverride(enchantmentId));
        return List.copyOf(override.supportedItems);
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
        return passesRarity(enchantmentId, "eldrich-surge:advanced_enchanting_table", stack, slot, level, 0);
    }

    public static boolean passesRarity(Identifier enchantmentId, String tableId, ItemStack stack, int slot, int level) {
        return passesRarity(enchantmentId, tableId, stack, slot, level, 0);
    }

    public static boolean passesRarity(Identifier enchantmentId, String tableId, ItemStack stack, int slot, int level, int seed) {
        CapsOverride override = data.enchantments.computeIfAbsent(enchantmentId.toString(), ignored -> defaultOverride(enchantmentId));
        int tableChance = tableId.equals("minecraft:enchanting_table")
                ? override.normalTableChance : override.advancedTableChance;
        int classWeight = switch (override.rarityClass) {
            case "uncommon" -> 60;
            case "rare" -> 30;
            case "very_rare" -> 5;
            default -> 100;
        };
        int rarity = Math.max(0, Math.min(100, tableChance)) * classWeight / 100;
        if (rarity >= 100) {
            return true;
        }
        if (rarity <= 0) {
            return false;
        }

        long combinedSeed = ((long) seed << 32) ^ java.util.Objects.hash(enchantmentId.toString(), stack == null || stack.isEmpty() ? "" : stack.getItem().toString(), slot, level);
        net.minecraft.util.RandomSource random = net.minecraft.util.RandomSource.create(combinedSeed);
        int roll = random.nextInt(100);
        return roll < rarity;
    }

    public static boolean isAvailableInEnchLib(Identifier enchantmentId) {
        return data.enchantments.computeIfAbsent(enchantmentId.toString(), ignored -> defaultOverride(enchantmentId)).enabled;
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

    private static boolean matchesEnchLibRules(List<String> rules, ItemStack stack) {
        boolean booksOnly = rules.stream().anyMatch(rule -> rule.equalsIgnoreCase("!all"));
        boolean positiveAll = rules.stream().anyMatch(rule -> rule.equalsIgnoreCase("all") || rule.equalsIgnoreCase("all_items"));
        boolean positiveMatch = positiveAll || rules.stream().filter(rule -> !rule.startsWith("!")).anyMatch(rule -> matchesItemRule(rule, stack));
        if (booksOnly) positiveMatch = stack.is(net.minecraft.world.item.Items.BOOK) || stack.is(net.minecraft.world.item.Items.ENCHANTED_BOOK);
        boolean excluded = rules.stream().filter(rule -> rule.startsWith("!") && !rule.equalsIgnoreCase("!all"))
                .anyMatch(rule -> matchesItemRule(rule.substring(1), stack));
        return positiveMatch && !excluded;
    }

    private static boolean matchesItemRule(String rawRule, ItemStack stack) {
        String rule = rawRule.toLowerCase(java.util.Locale.ROOT);
        Identifier itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (rule.startsWith("item:")) return itemId.toString().equals(rule.substring(5));
        if (rule.startsWith("#")) {
            Identifier tag = Identifier.tryParse(rule.substring(1));
            return tag != null && stack.is(TagKey.create(Registries.ITEM, tag));
        }
        if (rule.contains(":")) return itemId.toString().equals(rule);
        if (rule.equals("wooded")) return itemId.getPath().startsWith("wooden_");
        String tagName = switch (rule) {
            case "sword", "swords" -> "sword";
            case "axe", "axes" -> "axe";
            case "pickaxe", "pickaxes" -> "pickaxe";
            case "shovel", "shovels" -> "shovel";
            case "hoe", "hoes" -> "hoe";
            case "armor" -> "armor";
            case "weapon", "weapons" -> "weapon";
            case "bow", "bows" -> "bow";
            case "crossbow", "crossbows" -> "crossbow";
            case "trident", "tridents" -> "trident";
            case "mace", "maces" -> "mace";
            case "fishing_rod", "fishing_rods" -> "fishing";
            default -> null;
        };
        if (tagName != null) {
            Identifier tagId = Identifier.tryParse("minecraft:enchantable/" + tagName);
            if (tagId != null && stack.is(TagKey.create(Registries.ITEM, tagId))) return true;
            if (tagName.equals("armor")) return itemId.getPath().matches(".*_(helmet|chestplate|leggings|boots)$") || itemId.getPath().equals("turtle_helmet");
        }
        if (rule.equals("head_armor")) return itemId.getPath().endsWith("_helmet") || itemId.getPath().equals("turtle_helmet");
        if (rule.equals("turtle_shell")) return itemId.getPath().equals("turtle_helmet");
        if (rule.equals("chest_armor")) return itemId.getPath().endsWith("_chestplate");
        if (rule.equals("leg_armor")) return itemId.getPath().endsWith("_leggings");
        if (rule.equals("foot_armor")) return itemId.getPath().endsWith("_boots");
        if (rule.equals("tools")) return matchesItemRule("pickaxes", stack) || matchesItemRule("axes", stack) || matchesItemRule("shovels", stack) || matchesItemRule("hoes", stack);
        if (rule.equals("all")) return true;
        if (rule.equals("books")) return stack.is(net.minecraft.world.item.Items.BOOK) || stack.is(net.minecraft.world.item.Items.ENCHANTED_BOOK);
        if (List.of("wooden", "wood", "stone", "iron", "gold", "diamond", "netherite").contains(rule)) return itemId.getPath().startsWith(rule + "_");
        return itemId.getPath().equals(rule);
    }

    private static Object callEnchLib(String method, Class<?>[] parameterTypes, Object... args) {
        if (!FabricLoader.getInstance().isModLoaded("enchlib")) return null;
        try {
            Class<?> api = Class.forName("goldenrose01.enchlib.api.EnchantLibAPI");
            return api.getMethod(method, parameterTypes).invoke(null, args);
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return null;
        }
    }

    private static Data readBundledDefaults() {
        try (var stream = EnchantmentCapsConfig.class.getResourceAsStream("/config/eldrich-surge/enchantment_caps.json")) {
            if (stream == null) return new Data();
            Data loaded = GSON.fromJson(new java.io.InputStreamReader(stream, java.nio.charset.StandardCharsets.UTF_8), Data.class);
            if (loaded == null || loaded.enchantments == null) return new Data();
            return loaded;
        } catch (IOException | RuntimeException error) {
            EldritchSurge.LOGGER.error("Could not read bundled enchantment defaults", error);
            return new Data();
        }
    }

    private static Data readGlobalOrCreate() {
        Data defaults = readBundledDefaults();
        if (!Files.exists(CONFIG_PATH)) return defaults;
        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            Data loaded = GSON.fromJson(reader, Data.class);
            if (loaded == null) return defaults;
            if (loaded.enchantments == null) loaded.enchantments = new TreeMap<>();
            defaults.enchantments.forEach((id, value) -> loaded.enchantments.putIfAbsent(id, copyOverride(value)));
            return loaded;
        } catch (IOException | RuntimeException exception) {
            EldritchSurge.LOGGER.warn("Could not read {}, using defaults.", CONFIG_PATH, exception);
            return defaults;
        }
    }

    private static Data readWorldOrDefaults(Data defaults) {
        if (worldConfigPath == null || !Files.exists(worldConfigPath)) return copyData(defaults);
        try (Reader reader = Files.newBufferedReader(worldConfigPath)) {
            Data loaded = GSON.fromJson(reader, Data.class);
            if (loaded == null) return copyData(defaults);
            if (loaded.enchantments == null) loaded.enchantments = new TreeMap<>();
            defaults.enchantments.forEach((id, value) -> loaded.enchantments.putIfAbsent(id, copyOverride(value)));
            return loaded;
        } catch (IOException | RuntimeException exception) {
            EldritchSurge.LOGGER.warn("Could not read world settings at {}; using global defaults.", worldConfigPath, exception);
            return copyData(defaults);
        }
    }

    private static java.util.function.Consumer<String> clientSyncSender;

    public static void setClientSyncSender(java.util.function.Consumer<String> sender) {
        clientSyncSender = sender;
    }

    private static Data copyData(Data source) {
        return source == null ? new Data() : new Data(source);
    }

    private static CapsOverride copyOverride(CapsOverride source) {
        return source == null ? new CapsOverride() : new CapsOverride(source);
    }

    /** Reads EnchLib's effective world view once on the logical server, then the resulting snapshot is synced. */
    private static void captureEnchLibWorldValues() {
        if (!FabricLoader.getInstance().isModLoaded("enchlib")) return;
        for (Map.Entry<String, CapsOverride> entry : data.enchantments.entrySet()) {
            String id = entry.getKey();
            CapsOverride value = entry.getValue();
            Object enabled = callEnchLib("isEnabled", new Class<?>[]{String.class}, id);
            if (enabled instanceof Boolean configured) value.enabled = configured;
            Object maxLevel = callEnchLib("getMaxLevel", new Class<?>[]{String.class}, id);
            if (maxLevel instanceof Integer configured && configured > 0) {
                value.anvilMaxLevel = configured;
                value.enchantingTableMaxLevel = configured;
            }
            Object normalEnabled = callEnchLib("getTableEnabled", new Class<?>[]{String.class, String.class}, id, "minecraft:enchanting_table");
            if (normalEnabled instanceof Boolean configured) value.normalTable = configured;
            Object advancedEnabled = callEnchLib("getTableEnabled", new Class<?>[]{String.class, String.class}, id, "eldrich-surge:advanced_enchanting_table");
            if (advancedEnabled instanceof Boolean configured) value.advancedTable = configured;
            Object normalChance = callEnchLib("getTableChance", new Class<?>[]{String.class, String.class}, id, "minecraft:enchanting_table");
            if (normalChance instanceof Integer configured) value.normalTableChance = configured;
            Object advancedChance = callEnchLib("getTableChance", new Class<?>[]{String.class, String.class}, id, "eldrich-surge:advanced_enchanting_table");
            if (advancedChance instanceof Integer configured) value.advancedTableChance = configured;
            Object rarity = callEnchLib("getRarity", new Class<?>[]{String.class}, id);
            if (rarity instanceof String configured && List.of("common", "uncommon", "rare", "very_rare").contains(configured)) value.rarityClass = configured;
            Object categories = callEnchLib("getItemCategories", new Class<?>[]{String.class}, id);
            if (categories instanceof List<?> configured) value.supportedItems = configured.stream().filter(String.class::isInstance).map(String.class::cast).toList();
            Object incompatible = callEnchLib("getIncompatibleEnchantments", new Class<?>[]{String.class}, id);
            if (incompatible instanceof List<?> configured) value.incompatibleEnchantments = configured.stream().filter(String.class::isInstance).map(String.class::cast).toList();
        }
    }

    public static String resolveRarityClass(Identifier enchantmentId) {
        if (enchantmentId == null) return "common";
        CapsOverride override = data.enchantments.computeIfAbsent(enchantmentId.toString(), ignored -> defaultOverride(enchantmentId));
        return override.rarityClass != null ? override.rarityClass : "common";
    }

    private static String resolveRarityClassFromDefinition(ModEnchantmentDefinition definition) {
        if (definition == null || definition.classification() == null) return "common";
        String c = definition.classification().toLowerCase(java.util.Locale.ROOT);
        if (c.contains("ultra") || c.contains("legendary") || c.contains("unique") || c.contains("ritual") || c.contains("very")) {
            return "very_rare";
        }
        if (c.contains("rare") || c.contains("curse")) {
            return "rare";
        }
        if (c.contains("uncommon")) {
            return "uncommon";
        }
        return "common";
    }

    private static int defaultRarityForClass(String rarityClass) {
        if (rarityClass == null) return 100;
        return switch (rarityClass) {
            case "uncommon" -> 80;
            case "rare" -> 50;
            case "very_rare" -> 25;
            default -> 100;
        };
    }

    private static CapsOverride defaultOverride(Identifier enchantmentId) {
        CapsOverride override = new CapsOverride();
        if (enchantmentId == null) {
            override.loot = true;
            return override;
        }

        if (enchantmentId.getNamespace().equals("minecraft")) {
            override.normalTable = !isReplacedVanillaDamageEnchantment(enchantmentId);
        } else if (enchantmentId.getNamespace().equals(EldritchSurge.MOD_ID)) {
            String path = enchantmentId.getPath();
            ModEnchantmentDefinition definition = ModEnchantmentDefinitions.BY_ID.get(path);
            if (definition != null) {
                override.anvilMaxLevel = definition.maxLevel();
                override.enchantingTableMaxLevel = definition.maxLevel();
                override.normalTable = definition.normalTableDefault();
                override.advancedTable = definition.advancedTableDefault();
                override.loot = definition.lootDefault();
                override.supportedItems = new ArrayList<>(definition.supportedItems());
                override.rarityClass = resolveRarityClassFromDefinition(definition);
                override.rarity = defaultRarityForClass(override.rarityClass);
                override.normalTableChance = override.rarity;
                override.advancedTableChance = override.rarity;
            } else {
                override.advancedTable = true;
            }
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

    private static void disableReplacedVanillaEnchantments() {
        for (String id : List.of("minecraft:sharpness", "minecraft:smite", "minecraft:bane_of_arthropods")) {
            CapsOverride override = data.enchantments.computeIfAbsent(id, ignored -> new CapsOverride());
            override.normalTable = false;
            override.advancedTable = false;
            override.loot = false;
        }
    }

    private static boolean isReplacedVanillaDamageEnchantment(Identifier enchantmentId) {
        String id = enchantmentId.toString();
        return id.equals("minecraft:sharpness")
                || id.equals("minecraft:smite")
                || id.equals("minecraft:bane_of_arthropods");
    }

    private static void applyBaseLevelPresets() {
        for (ModEnchantmentDefinition definition : ModEnchantmentDefinitions.ALL) {
            CapsOverride override = data.enchantments.computeIfAbsent(EldritchSurge.MOD_ID + ":" + definition.id(), ignored -> defaultOverride(EldritchSurge.id(definition.id())));
            if (override.anvilMaxLevel <= 0) {
                override.anvilMaxLevel = definition.maxLevel();
            }
            if (override.enchantingTableMaxLevel <= 0) {
                override.enchantingTableMaxLevel = definition.maxLevel();
            }
        }
    }

    public static void save() {
        if (FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT && !clientWorldSnapshot) return;
        if (clientWorldSnapshot) {
            if (clientCanEditWorld && clientSyncSender != null) clientSyncSender.accept(serializeForSync());
            return;
        }
        Path target = worldConfigPath == null ? CONFIG_PATH : worldConfigPath;
        try {
            Files.createDirectories(target.getParent());
            try (Writer writer = Files.newBufferedWriter(target)) {
                GSON.toJson(data, writer);
            }
            if (worldConfigPath != null) eldritch.surge.network.WorldEnchantConfigSync.broadcast(activeServer);
        } catch (IOException exception) {
            EldritchSurge.LOGGER.error("Could not save {}.", target, exception);
        }
    }

    public static boolean isKeepLoyaltyTridents() {
        return data.keepLoyaltyTridents;
    }

    public static void setKeepLoyaltyTridents(boolean value) {
        data.keepLoyaltyTridents = value;
    }

    public static boolean isSweepingRequiresEnchantment() {
        return data.sweepingRequiresEnchantment;
    }

    public static void setSweepingRequiresEnchantment(boolean value) {
        data.sweepingRequiresEnchantment = value;
    }

    public static String getLethalPoisonMode() {
        return data.getLethalPoisonMode();
    }

    public static void setLethalPoisonMode(String mode) {
        data.setLethalPoisonMode(mode);
    }

    public static boolean isLethalPoison() {
        return !"off".equalsIgnoreCase(data.getLethalPoisonMode());
    }

    public static void setLethalPoison(boolean value) {
        data.setLethalPoisonMode(value ? "all" : "off");
    }

    public static void saveGlobal() {
        Path previous = worldConfigPath;
        worldConfigPath = null;
        save();
        worldConfigPath = previous;
    }

    private enum CapKind {
        ANVIL,
        ENCHANTING_TABLE
    }

    public static final class Data {
        public int configVersion = 4;
        public boolean keepLoyaltyTridents = false;
        public boolean sweepingRequiresEnchantment = true;
        private com.google.gson.JsonElement lethalPoison = new com.google.gson.JsonPrimitive("off");
        public Map<String, CapsOverride> enchantments = new TreeMap<>();

        public Data() {}

        public Data(Data other) {
            if (other != null) {
                this.configVersion = other.configVersion;
                this.keepLoyaltyTridents = other.keepLoyaltyTridents;
                this.sweepingRequiresEnchantment = other.sweepingRequiresEnchantment;
                this.lethalPoison = other.lethalPoison == null ? new com.google.gson.JsonPrimitive("off") : other.lethalPoison.deepCopy();
                if (other.enchantments != null) {
                    for (var entry : other.enchantments.entrySet()) {
                        this.enchantments.put(entry.getKey(), new CapsOverride(entry.getValue()));
                    }
                }
            }
        }

        public String getLethalPoisonMode() {
            if (lethalPoison == null || lethalPoison.isJsonNull()) return "off";
            if (lethalPoison.isJsonPrimitive()) {
                var prim = lethalPoison.getAsJsonPrimitive();
                if (prim.isBoolean()) {
                    return prim.getAsBoolean() ? "all" : "off";
                }
                return prim.getAsString();
            }
            return "off";
        }

        public void setLethalPoisonMode(String mode) {
            this.lethalPoison = new com.google.gson.JsonPrimitive(mode == null ? "off" : mode.toLowerCase(java.util.Locale.ROOT));
        }
    }

    public static final class CapsOverride {
        public boolean enabled = true;
        public int anvilMaxLevel = 0;
        public int enchantingTableMaxLevel = 0;
        public boolean normalTable = false;
        public boolean advancedTable = false;
        public boolean loot = false;
        public int rarity = 100;
        public String rarityClass = "common";
        public int normalTableChance = 100;
        public int advancedTableChance = 100;
        public List<String> supportedItems = new ArrayList<>();
        public List<String> incompatibleEnchantments = new ArrayList<>();

        public CapsOverride() {}

        public CapsOverride(CapsOverride other) {
            if (other != null) {
                this.enabled = other.enabled;
                this.anvilMaxLevel = other.anvilMaxLevel;
                this.enchantingTableMaxLevel = other.enchantingTableMaxLevel;
                this.normalTable = other.normalTable;
                this.advancedTable = other.advancedTable;
                this.loot = other.loot;
                this.rarity = other.rarity;
                this.rarityClass = other.rarityClass;
                this.normalTableChance = other.normalTableChance;
                this.advancedTableChance = other.advancedTableChance;
                this.supportedItems = new ArrayList<>(other.supportedItems);
                this.incompatibleEnchantments = new ArrayList<>(other.incompatibleEnchantments);
            }
        }
    }
}
