package eldritch.surge.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import eldritch.surge.config.EnchantmentCapsConfig;
import eldritch.surge.enchantment.EnchantmentIndex;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Map;

public final class EldritchModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return EldritchModMenuIntegration::createConfigScreen;
    }

    private static Screen createConfigScreen(Screen parent) {
        EnchantmentIndex.refreshFromRegistry();
        EnchantmentCapsConfig.loadAndSyncWithRegistry(EnchantmentIndex.snapshot().keySet());

        ConfigCategory.Builder category = ConfigCategory.createBuilder()
                .name(Text.translatable("config.eldritch-surge.level_caps"));

        for (Map.Entry<String, EnchantmentCapsConfig.CapsOverride> entry : EnchantmentCapsConfig.enchantmentOverrides().entrySet()) {
            String enchantmentId = entry.getKey();

            category.group(OptionGroup.createBuilder()
                    .name(Text.literal(enchantmentId))
                    .description(OptionDescription.of(Text.translatable("config.eldritch-surge.level_caps.group_description")))
                    .option(Option.<Integer>createBuilder()
                            .name(Text.translatable("config.eldritch-surge.anvil_max_level"))
                            .description(OptionDescription.of(Text.translatable("config.eldritch-surge.zero_uses_vanilla")))
                            .binding(
                                    0,
                                    () -> EnchantmentCapsConfig.getAnvilCap(enchantmentId),
                                    value -> EnchantmentCapsConfig.setAnvilCap(enchantmentId, value)
                            )
                            .controller(IntegerFieldControllerBuilder::create)
                            .build())
                    .option(Option.<Integer>createBuilder()
                            .name(Text.translatable("config.eldritch-surge.enchanting_table_max_level"))
                            .description(OptionDescription.of(Text.translatable("config.eldritch-surge.zero_uses_vanilla")))
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
                .title(Text.translatable("config.eldritch-surge.title"))
                .category(category.build())
                .save(EnchantmentCapsConfig::save)
                .build()
                .generateScreen(parent);
    }
}
