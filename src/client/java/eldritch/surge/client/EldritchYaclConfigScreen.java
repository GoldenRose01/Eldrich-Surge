package eldritch.surge.client;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import eldritch.surge.EldritchSurge;
import eldritch.surge.config.EnchantmentCapsConfig;
import eldritch.surge.config.EnchantmentCapsConfig.CapsOverride;
import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinitions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

final class EldritchYaclConfigScreen {
    private EldritchYaclConfigScreen() {}

    static net.minecraft.client.gui.screens.Screen create(net.minecraft.client.gui.screens.Screen parent) {
        boolean editable = EnchantmentCapsConfig.canEditWorldRules();

        YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("config.eldrich-surge.title"))
                .save(EnchantmentCapsConfig::save);

        // 1. General Settings Category
        ConfigCategory.Builder generalCategory = ConfigCategory.createBuilder()
                .name(Component.translatable("config.eldrich-surge.category.general"));

        OptionGroup.Builder gameplayGroup = OptionGroup.createBuilder()
                .name(Component.translatable("config.eldrich-surge.group.gameplay"))
                .collapsed(false)
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("config.eldrich-surge.keep_loyalty_tridents"))
                        .description(OptionDescription.of(Component.translatable("config.eldrich-surge.keep_loyalty_tridents.description")))
                        .binding(
                                false,
                                EnchantmentCapsConfig::isKeepLoyaltyTridents,
                                EnchantmentCapsConfig::setKeepLoyaltyTridents
                        )
                        .available(editable)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("config.eldrich-surge.sweeping_requires_enchantment"))
                        .description(OptionDescription.of(Component.translatable("config.eldrich-surge.sweeping_requires_enchantment.description")))
                        .binding(
                                true,
                                EnchantmentCapsConfig::isSweepingRequiresEnchantment,
                                EnchantmentCapsConfig::setSweepingRequiresEnchantment
                        )
                        .available(editable)
                        .controller(BooleanControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable("config.eldrich-surge.lethal_poison"))
                        .description(OptionDescription.of(Component.translatable("config.eldrich-surge.lethal_poison.description")))
                        .binding(
                                false,
                                EnchantmentCapsConfig::isLethalPoison,
                                EnchantmentCapsConfig::setLethalPoison
                        )
                        .available(editable)
                        .controller(BooleanControllerBuilder::create)
                        .build());

        generalCategory.group(gameplayGroup.build());

        OptionGroup.Builder infoGroup = OptionGroup.createBuilder()
                .name(Component.translatable("config.eldrich-surge.group.info"))
                .collapsed(false)
                .option(Option.<Boolean>createBuilder()
                        .name(Component.translatable(editable ? "config.eldrich-surge.world_editable" : "config.eldrich-surge.world_read_only"))
                        .binding(editable, () -> editable, v -> {})
                        .available(false)
                        .controller(BooleanControllerBuilder::create)
                        .build());
        generalCategory.group(infoGroup.build());

        builder.category(generalCategory.build());

        // 2. Enchantment Categories
        ConfigCategory.Builder weaponsCategory = ConfigCategory.createBuilder()
                .name(Component.translatable("config.eldrich-surge.category.weapons"));
        ConfigCategory.Builder armorCategory = ConfigCategory.createBuilder()
                .name(Component.translatable("config.eldrich-surge.category.armor"));
        ConfigCategory.Builder rangedCategory = ConfigCategory.createBuilder()
                .name(Component.translatable("config.eldrich-surge.category.ranged"));
        ConfigCategory.Builder toolsCategory = ConfigCategory.createBuilder()
                .name(Component.translatable("config.eldrich-surge.category.tools"));
        ConfigCategory.Builder magicCategory = ConfigCategory.createBuilder()
                .name(Component.translatable("config.eldrich-surge.category.magic"));
        ConfigCategory.Builder vanillaCategory = ConfigCategory.createBuilder()
                .name(Component.translatable("config.eldrich-surge.category.vanilla"));

        int weaponsCount = 0;
        int armorCount = 0;
        int rangedCount = 0;
        int toolsCount = 0;
        int magicCount = 0;
        int vanillaCount = 0;

        for (Map.Entry<String, CapsOverride> entry : new TreeMap<>(EnchantmentCapsConfig.enchantmentOverrides()).entrySet()) {
            String enchantmentId = entry.getKey();
            Identifier id = Identifier.tryParse(enchantmentId);
            if (id == null) continue;

            CapsOverride defaultVals = EnchantmentCapsConfig.defaultOverride(id);
            int defAnvil = defaultVals.anvilMaxLevel;
            int defTable = defaultVals.enchantingTableMaxLevel;
            boolean defNormal = defaultVals.normalTable;
            boolean defAdvanced = defaultVals.advancedTable;
            boolean defLoot = defaultVals.loot;

            Component enchantmentName = Component.translatable("enchantment." + enchantmentId.replace(':', '.'));

            OptionGroup group = OptionGroup.createBuilder()
                    .name(enchantmentName)
                    .description(OptionDescription.of(Component.translatable("config.eldrich-surge.level_caps.group_description")))
                    .collapsed(true)
                    .option(Option.<Boolean>createBuilder()
                            .name(Component.translatable("config.eldrich-surge.normal_table"))
                            .description(OptionDescription.of(Component.translatable("config.eldrich-surge.normal_table.description")))
                            .binding(
                                    defNormal,
                                    () -> EnchantmentCapsConfig.isAllowedInNormalTable(id),
                                    value -> EnchantmentCapsConfig.setAllowedInNormalTable(enchantmentId, value)
                            )
                            .available(editable)
                            .controller(BooleanControllerBuilder::create)
                            .build())
                    .option(Option.<Boolean>createBuilder()
                            .name(Component.translatable("config.eldrich-surge.advanced_table"))
                            .description(OptionDescription.of(Component.translatable("config.eldrich-surge.advanced_table.description")))
                            .binding(
                                    defAdvanced,
                                    () -> EnchantmentCapsConfig.isAllowedInAdvancedTable(id),
                                    value -> EnchantmentCapsConfig.setAllowedInAdvancedTable(enchantmentId, value)
                            )
                            .available(editable)
                            .controller(BooleanControllerBuilder::create)
                            .build())
                    .option(Option.<Boolean>createBuilder()
                            .name(Component.translatable("config.eldrich-surge.loot"))
                            .description(OptionDescription.of(Component.translatable("config.eldrich-surge.loot.description")))
                            .binding(
                                    defLoot,
                                    () -> EnchantmentCapsConfig.isAllowedAsLoot(id),
                                    value -> EnchantmentCapsConfig.setAllowedAsLoot(enchantmentId, value)
                            )
                            .available(editable)
                            .controller(BooleanControllerBuilder::create)
                            .build())
                    .option(Option.<Integer>createBuilder()
                            .name(Component.translatable("config.eldrich-surge.anvil_max_level"))
                            .description(OptionDescription.of(Component.translatable("config.eldrich-surge.zero_uses_vanilla")))
                            .binding(
                                    defAnvil,
                                    () -> EnchantmentCapsConfig.getAnvilCap(enchantmentId),
                                    value -> EnchantmentCapsConfig.setAnvilCap(enchantmentId, value)
                            )
                            .available(editable)
                            .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(0).max(255))
                            .build())
                    .option(Option.<Integer>createBuilder()
                            .name(Component.translatable("config.eldrich-surge.enchanting_table_max_level"))
                            .description(OptionDescription.of(Component.translatable("config.eldrich-surge.zero_uses_vanilla")))
                            .binding(
                                    defTable,
                                    () -> EnchantmentCapsConfig.getEnchantingTableCap(enchantmentId),
                                    value -> EnchantmentCapsConfig.setEnchantingTableCap(enchantmentId, value)
                            )
                            .available(editable)
                            .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(0).max(255))
                            .build())
                    .build();

            EnchantmentTab tab = resolveTab(id);
            switch (tab) {
                case WEAPONS -> { weaponsCategory.group(group); weaponsCount++; }
                case ARMOR -> { armorCategory.group(group); armorCount++; }
                case RANGED -> { rangedCategory.group(group); rangedCount++; }
                case TOOLS -> { toolsCategory.group(group); toolsCount++; }
                case MAGIC -> { magicCategory.group(group); magicCount++; }
                case VANILLA -> { vanillaCategory.group(group); vanillaCount++; }
            }
        }

        if (weaponsCount > 0) builder.category(weaponsCategory.build());
        if (armorCount > 0) builder.category(armorCategory.build());
        if (rangedCount > 0) builder.category(rangedCategory.build());
        if (toolsCount > 0) builder.category(toolsCategory.build());
        if (magicCount > 0) builder.category(magicCategory.build());
        if (vanillaCount > 0) builder.category(vanillaCategory.build());

        return builder.build().generateScreen(parent);
    }

    private enum EnchantmentTab {
        WEAPONS,
        ARMOR,
        RANGED,
        TOOLS,
        MAGIC,
        VANILLA
    }

    private static EnchantmentTab resolveTab(Identifier id) {
        if ("minecraft".equals(id.getNamespace())) {
            return EnchantmentTab.VANILLA;
        }
        if (EldritchSurge.MOD_ID.equals(id.getNamespace())) {
            ModEnchantmentDefinition def = ModEnchantmentDefinitions.BY_ID.get(id.getPath());
            if (def != null) {
                String cat = def.itemCategory().toUpperCase(Locale.ROOT);
                if (cat.contains("BOW") || cat.contains("CROSSBOW")) return EnchantmentTab.RANGED;
                if (cat.contains("ARMOR") || cat.contains("HELMET") || cat.contains("CHEST")
                        || cat.contains("LEGGING") || cat.contains("BOOT") || cat.contains("ELYTRA")) return EnchantmentTab.ARMOR;
                if (cat.contains("WEAPON") || cat.contains("SWORD") || cat.contains("MACE")
                        || cat.contains("SPEAR") || cat.contains("TRIDENT")) return EnchantmentTab.WEAPONS;
                if (cat.contains("TOOL") || cat.contains("PICKAXE") || cat.contains("SHOVEL")
                        || cat.contains("HOE") || cat.contains("SHEAR") || cat.contains("FISHING") || cat.contains("SHIELD") || cat.contains("AXE")) return EnchantmentTab.TOOLS;
                return EnchantmentTab.MAGIC;
            }
        }
        return EnchantmentTab.WEAPONS;
    }
}
