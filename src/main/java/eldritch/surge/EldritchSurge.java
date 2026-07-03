package eldritch.surge;

import eldritch.surge.config.EnchantmentCapsConfig;
import eldritch.surge.creative.EldritchCreativeTabs;
import eldritch.surge.command.EldritchCommands;
import eldritch.surge.block.EldritchBlocks;
import eldritch.surge.enchantment.EldritchEnchantmentEffects;
import eldritch.surge.enchantment.EldritchEnchantmentGroups;
import eldritch.surge.enchantment.EnchantmentIndex;
import eldritch.surge.enchantment.mechanics.DiggerMiningMechanic;
import eldritch.surge.entity.EldritchEntityTaxonomy;
import eldritch.surge.game.EldritchGameRules;
import eldritch.surge.menu.EldritchMenus;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EldritchSurge implements ModInitializer {
    public static final String MOD_ID = "eldritch-surge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        EldritchGameRules.initialize();
        EldritchMenus.initialize();
        EldritchBlocks.initialize();
        EldritchEnchantmentEffects.initialize();
        EldritchEnchantmentGroups.initialize();
        EldritchEntityTaxonomy.initialize();
        EldritchCreativeTabs.initialize();
        EldritchCommands.initialize();
        DiggerMiningMechanic.initialize();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            EnchantmentIndex.refreshFromRegistry(server.registryAccess().lookupOrThrow(Registries.ENCHANTMENT));
            EnchantmentCapsConfig.loadAndSyncWithRegistry(EnchantmentIndex.snapshot().keySet());

            LOGGER.info("Eldritch Surge synchronized {} enchantments from the dynamic registry.",
                    EnchantmentIndex.snapshot().size());
        });

        LOGGER.info("Eldritch Surge initialized.");
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
