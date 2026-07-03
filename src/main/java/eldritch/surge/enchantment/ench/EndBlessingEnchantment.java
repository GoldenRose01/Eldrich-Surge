package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class EndBlessingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "end_blessing",
            "End Blessing",
            "HELMET",
            1,
            "Legendary",
            "Advanced",
            List.of("#minecraft:head_armor"),
            List.of("head"),
            "#eldritch-surge:blessings",
            "La barra della saturazione del giocatore non diminuisce mentre ci si trova nella dimensione dell'End."
    );

    private EndBlessingEnchantment() {
    }
}

