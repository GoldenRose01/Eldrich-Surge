package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CatapultEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "catapult",
            "Catapult",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            2,
            "Uncommon",
            "Advanced",
            List.of("#minecraft:swords", "#minecraft:axes"),
            List.of("hand"),
            "",
            "Un attacco critico andato a segno scaraventa il target colpito in aria."
    );

    private CatapultEnchantment() {
    }
}

