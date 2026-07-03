package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HeartOfDepthEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "heart_of_depth",
            "Heart of Depth",
            "CHESTPLATE",
            1,
            "Legendary",
            "Advanced",
            List.of("#minecraft:chest_armor"),
            List.of("chest"),
            "#eldritch-surge:hearts",
            "Quando si subisce danno, applica l'effetto di stato Oscurità (Darkness) all'attaccante."
    );

    private HeartOfDepthEnchantment() {
    }
}

