package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HellBlessingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "hell_blessing",
            "Hell Blessing",
            "HELMET",
            5,
            "Legendary",
            "Advanced",
            List.of("#minecraft:head_armor"),
            List.of("head"),
            "#eldritch-surge:blessings",
            "Trovarsi nella lava o nel fuoco genera livello * 10 sfere di XP al secondo."
    );

    private HellBlessingEnchantment() {
    }
}

