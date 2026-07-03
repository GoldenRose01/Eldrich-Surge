package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class TriumphEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "triumph",
            "Triumph",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            3,
            "Legendary",
            "Advanced",
            List.of("#minecraft:swords"),
            List.of("hand"),
            "",
            "Eliminare un nemico rigenera istantaneamente una quota fissa di salute o barra della saturazione alimentare."
    );

    private TriumphEnchantment() {
    }
}

