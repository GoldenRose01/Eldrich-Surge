package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class EndAdaptabilityEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "end_adaptability",
            "End Adaptability",
            "LEGGINGS",
            1,
            "Ultra",
            "Advanced",
            List.of("#minecraft:leg_armor"),
            List.of("legs"),
            "",
            "Annulla completamente i danni da caduta nel Vuoto (Void damage), teletrasportando il giocatore sulla terraferma sicura più vicina."
    );

    private EndAdaptabilityEnchantment() {
    }
}

