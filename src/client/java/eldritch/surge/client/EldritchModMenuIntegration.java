package eldritch.surge.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import eldritch.surge.config.EnchantmentCapsConfig;
import net.minecraft.network.chat.Component;

import java.util.Map;

public final class EldritchModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::createConfigScreen;
    }

    private net.minecraft.client.gui.screens.Screen createConfigScreen(net.minecraft.client.gui.screens.Screen parent) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder()
                .name(Component.translatable("config.eldritch-surge.level_caps"));

        for (Map.Entry<String, ?> entry : EnchantmentCapsConfig.enchantmentOverrides().entrySet()) {
            String enchantmentId = entry.getKey();
            Component enchantmentName = Component.translatable("enchantment." + enchantmentId.replace(':', '.'));

            category.group(OptionGroup.createBuilder()
                    .name(enchantmentName)
                    .description(OptionDescription.of(Component.translatable("config.eldritch-surge.level_caps.group_description")))
                    .option(Option.<Boolean>createBuilder()
                            .name(Component.translatable("config.eldritch-surge.normal_table"))
                            .description(OptionDescription.of(Component.translatable("config.eldritch-surge.normal_table.description")))
                            .binding(
                                    false,
                                    () -> EnchantmentCapsConfig.isAllowedInNormalTable(net.minecraft.resources.Identifier.tryParse(enchantmentId)),
                                    value -> EnchantmentCapsConfig.setAllowedInNormalTable(enchantmentId, value)
                            )
                            .controller(BooleanControllerBuilder::create)
                            .build())
                    .option(Option.<Boolean>createBuilder()
                            .name(Component.translatable("config.eldritch-surge.advanced_table"))
                            .description(OptionDescription.of(Component.translatable("config.eldritch-surge.advanced_table.description")))
                            .binding(
                                    false,
                                    () -> EnchantmentCapsConfig.isAllowedInAdvancedTable(net.minecraft.resources.Identifier.tryParse(enchantmentId)),
                                    value -> EnchantmentCapsConfig.setAllowedInAdvancedTable(enchantmentId, value)
                            )
                            .controller(BooleanControllerBuilder::create)
                            .build())
                    .option(Option.<Boolean>createBuilder()
                            .name(Component.translatable("config.eldritch-surge.loot"))
                            .description(OptionDescription.of(Component.translatable("config.eldritch-surge.loot.description")))
                            .binding(
                                    false,
                                    () -> EnchantmentCapsConfig.isAllowedAsLoot(net.minecraft.resources.Identifier.tryParse(enchantmentId)),
                                    value -> EnchantmentCapsConfig.setAllowedAsLoot(enchantmentId, value)
                            )
                            .controller(BooleanControllerBuilder::create)
                            .build())
                    .option(Option.<Integer>createBuilder()
                            .name(Component.translatable("config.eldritch-surge.anvil_max_level"))
                            .description(OptionDescription.of(Component.translatable("config.eldritch-surge.zero_uses_vanilla")))
                            .binding(
                                    0,
                                    () -> EnchantmentCapsConfig.getAnvilCap(enchantmentId),
                                    value -> EnchantmentCapsConfig.setAnvilCap(enchantmentId, value)
                            )
                            .controller(IntegerFieldControllerBuilder::create)
                            .build())
                    .option(Option.<Integer>createBuilder()
                            .name(Component.translatable("config.eldritch-surge.enchanting_table_max_level"))
                            .description(OptionDescription.of(Component.translatable("config.eldritch-surge.zero_uses_vanilla")))
                            .binding(
                                    0,
                                    () -> EnchantmentCapsConfig.getEnchantingTableCap(enchantmentId),
                                    value -> EnchantmentCapsConfig.setEnchantingTableCap(enchantmentId, value)
                            )
                            .controller(IntegerFieldControllerBuilder::create)
                            .build())
                    .build());
        }

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("config.eldritch-surge.title"))
                .category(category.build())
                .save(EnchantmentCapsConfig::save)
                .build()
                .generateScreen(parent);
    }
}
