package eldritch.surge.enchantment;

import eldritch.surge.config.EnchantmentCapsConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resources.Identifier;

public final class EnchantmentLevelCaps {
    private EnchantmentLevelCaps() {
    }

    public static int forAnvil(RegistryKey<Enchantment> enchantment, int vanillaCap) {
        return EnchantmentCapsConfig.resolveAnvilCap(enchantment.getValue(), vanillaCap);
    }

    public static int forAnvil(Identifier enchantmentId, int vanillaCap) {
        return EnchantmentCapsConfig.resolveAnvilCap(enchantmentId, vanillaCap);
    }

    public static int forEnchantingTable(RegistryKey<Enchantment> enchantment, int vanillaCap) {
        return EnchantmentCapsConfig.resolveEnchantingTableCap(enchantment.getValue(), vanillaCap);
    }

    public static int forEnchantingTable(Identifier enchantmentId, int vanillaCap) {
        return EnchantmentCapsConfig.resolveEnchantingTableCap(enchantmentId, vanillaCap);
    }
}
