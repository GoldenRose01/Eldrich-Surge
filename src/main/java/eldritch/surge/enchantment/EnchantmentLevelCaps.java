package eldritch.surge.enchantment;

import eldritch.surge.config.EnchantmentCapsConfig;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

public final class EnchantmentLevelCaps {
    private EnchantmentLevelCaps() {
    }

    public static int forAnvil(ResourceKey<Enchantment> enchantment, int vanillaCap) {
        return EnchantmentCapsConfig.resolveAnvilCap(enchantment.identifier(), vanillaCap);
    }

    public static int forAnvil(Identifier enchantmentId, int vanillaCap) {
        return EnchantmentCapsConfig.resolveAnvilCap(enchantmentId, vanillaCap);
    }

    public static int forEnchantingTable(ResourceKey<Enchantment> enchantment, int vanillaCap) {
        return EnchantmentCapsConfig.resolveEnchantingTableCap(enchantment.identifier(), vanillaCap);
    }

    public static int forEnchantingTable(Identifier enchantmentId, int vanillaCap) {
        return EnchantmentCapsConfig.resolveEnchantingTableCap(enchantmentId, vanillaCap);
    }
}
