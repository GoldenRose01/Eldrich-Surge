package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class RedMoonEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "red_moon",
            "Red Moon",
            "CAST SPELLS (RITUALI AUTOMATICI)",
            1,
            "Ritual",
            "Advanced",
            List.of("#minecraft:enchantable/durability"),
            List.of("hand"),
            "#eldritch-surge:rituals",
            "Evoca un'ondata di 30 Zombie dotati di equipaggiamento in diamante e varianti zombie horse."
    );

    private RedMoonEnchantment() {
    }
}

