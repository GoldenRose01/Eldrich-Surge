package eldritch.surge.enchantment;

import eldritch.surge.EldritchSurge;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class EldritchEnchantments {
    public static final RegistryKey<Enchantment> BANE_OF_END = of("bane_of_end");
    public static final RegistryKey<Enchantment> UNDEAD_SLAYER = of("undead_slayer");
    public static final RegistryKey<Enchantment> BLADE_OF_APOCALYPSE = of("blade_of_apocalypse");

    private EldritchEnchantments() {
    }

    private static RegistryKey<Enchantment> of(String path) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, EldritchSurge.id(path));
    }
}
