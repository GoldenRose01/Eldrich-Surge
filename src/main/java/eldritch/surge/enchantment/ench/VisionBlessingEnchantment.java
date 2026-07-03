package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class VisionBlessingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "vision_blessing",
            "Vision Blessing",
            "HELMET",
            1,
            "Legendary",
            "Advanced",
            List.of("#minecraft:head_armor"),
            List.of("head"),
            "#eldritch-surge:blessings",
            "Fornisce l'effetto di stato Visione Notturna permanente."
    );

    private VisionBlessingEnchantment() {
    }
}

