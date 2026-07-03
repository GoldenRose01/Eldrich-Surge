package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CometSlamEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "comet_slam",
            "Comet Slam",
            "MACE (NATIVO VANILLA)",
            1,
            "Legendary",
            "Advanced",
            List.of("#minecraft:enchantable/mace"),
            List.of("hand"),
            "#eldritch-surge:additional_damage",
            "Se lo Smash Attack viene eseguito da piu di 12 blocchi d'altezza, genera un'esplosione magica che incendia l'area e applica Knockback X."
    );

    private CometSlamEnchantment() {
    }
}
