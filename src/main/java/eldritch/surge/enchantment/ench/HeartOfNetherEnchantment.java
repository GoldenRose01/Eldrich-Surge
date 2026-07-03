package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HeartOfNetherEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "heart_of_nether",
            "Heart of Nether",
            "CHESTPLATE",
            1,
            "Legendary",
            "Advanced",
            List.of("#minecraft:chest_armor"),
            List.of("chest"),
            "#eldritch-surge:hearts",
            "Dà fuoco automaticamente a qualsiasi entità che attacca il giocatore in mischia."
    );

    private HeartOfNetherEnchantment() {
    }
}

