package eldritch.surge;

import eldritch.surge.config.EnchantmentCapsConfig;
import eldritch.surge.enchantment.EldritchEnchantmentEffects;
import eldritch.surge.enchantment.EldritchEnchantmentGroups;
import eldritch.surge.enchantment.EnchantmentIndex;
import eldritch.surge.entity.EldritchEntityCategories;
import eldritch.surge.game.EldritchGameRules;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EldritchSurge implements ModInitializer {
    public static final String MOD_ID = "eldritch-surge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        EldritchGameRules.initialize();
        EldritchEnchantmentEffects.initialize();
        EldritchEnchantmentGroups.initialize();
        EldritchEntityCategories.initialize();

        EnchantmentIndex.refreshFromRegistry();
        EnchantmentCapsConfig.loadAndSyncWithRegistry(EnchantmentIndex.snapshot().keySet());

        LOGGER.info("Eldritch Surge initialized with {} enchantments visible in the registry.",
                EnchantmentIndex.snapshot().size());
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
