package eldritch.surge.enchantment;

import eldritch.surge.EldritchSurge;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;

public final class EldritchEnchantmentGroups {
    public static final TagKey<Enchantment> ADDITIONAL_DAMAGE = of("additional_damage");
    public static final TagKey<Enchantment> WEAPON_UTILITY = of("weapon_utility");
    public static final TagKey<Enchantment> ASPECT = of("aspect");
    public static final TagKey<Enchantment> OFFHAND = of("offhand");

    private EldritchEnchantmentGroups() {
    }

    public static void initialize() {
        EldritchSurge.LOGGER.debug("Loaded Eldritch Surge enchantment group tags.");
    }

    private static TagKey<Enchantment> of(String path) {
        return TagKey.create(Registries.ENCHANTMENT, EldritchSurge.id(path));
    }
}
