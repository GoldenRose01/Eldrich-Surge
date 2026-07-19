package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class GreamReaperEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "gream_reaper",
            "Gream Reaper",
            "NETHERITE HOE",
            10,
            "Unique",
            "Normal",
            List.of("minecraft:netherite_hoe"),
            List.of("hand"),
            "",
            "Sbloccabile unendo Blade of Apocalypse + Star Fate. Aggiunge un bonus di livello cumulativo."
    );

    private GreamReaperEnchantment() {
    }
}

