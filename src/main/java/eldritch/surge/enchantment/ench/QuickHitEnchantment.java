package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class QuickHitEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "quick_hit",
            "Quick Hit",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            5,
            "Epic",
            "Advanced",
            List.of("#minecraft:swords", "#minecraft:axes"),
            List.of("hand"),
            "",
            "Riduce il cooldown di ricarica della barra di attacco, permettendo di colpire più velocemente a piena potenza."
    );

    private QuickHitEnchantment() {
    }
}

