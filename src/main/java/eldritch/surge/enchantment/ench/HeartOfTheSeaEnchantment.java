package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HeartOfTheSeaEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "heart_of_the_sea",
            "Heart of the Sea",
            "CHESTPLATE",
            1,
            "Legendary",
            "Advanced",
            List.of("#minecraft:chest_armor"),
            List.of("chest"),
            "#eldritch-surge:hearts",
            "Fornisce gli effetti di stato Conduit Power e Dolphin's Grace permanenti al giocatore."
    );

    private HeartOfTheSeaEnchantment() {
    }
}

