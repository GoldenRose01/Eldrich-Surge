package eldritch.surge.enchantment;

import eldritch.surge.EldritchSurge;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

public final class EldritchEnchantments {
    public static final ResourceKey<Enchantment> BANE_OF_END = of("bane_of_end");
    public static final ResourceKey<Enchantment> UNDEAD_SLAYER = of("undead_slayer");
    public static final ResourceKey<Enchantment> BLADE_OF_APOCALYPSE = of("blade_of_apocalypse");

    private EldritchEnchantments() {
    }

    private static ResourceKey<Enchantment> of(String path) {
        return ResourceKey.create(Registries.ENCHANTMENT, EldritchSurge.id(path));
    }
}
