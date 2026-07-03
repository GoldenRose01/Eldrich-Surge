package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class BackslashEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "backslash",
            "Backslash",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            5,
            "Rare",
            "Normal",
            List.of("#minecraft:axes"),
            List.of("hand"),
            "",
            "Respingerà il nemico applicando l'effetto di stato Debolezza (Weakness) proporzionale al livello."
    );

    private BackslashEnchantment() {
    }
}

