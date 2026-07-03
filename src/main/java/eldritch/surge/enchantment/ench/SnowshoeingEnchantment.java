package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SnowshoeingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "snowshoeing",
            "Snowshoeing",
            "BOOTS",
            1,
            "Legendary",
            "Advanced",
            List.of("#minecraft:foot_armor"),
            List.of("feet"),
            "",
            "Permette al giocatore di camminare sopra la neve polverosa senza sprofondare e senza subire congelamento."
    );

    private SnowshoeingEnchantment() {
    }
}

