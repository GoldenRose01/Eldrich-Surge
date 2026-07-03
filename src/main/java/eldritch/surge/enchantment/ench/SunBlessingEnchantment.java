package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SunBlessingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "sun_blessing",
            "Sun Blessing",
            "HELMET",
            5,
            "Legendary",
            "Advanced",
            List.of("#minecraft:head_armor"),
            List.of("head"),
            "#eldritch-surge:blessings",
            "Genera livello * 10 sfere di XP ogni minuto se esposto direttamente alla luce solare diretta."
    );

    private SunBlessingEnchantment() {
    }
}

