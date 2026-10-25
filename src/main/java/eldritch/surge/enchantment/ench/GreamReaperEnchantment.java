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
            "Aggiunge 2,5 danni per livello contro ogni creatura: al livello 10 equivale a Smite X contro tutte le categorie."
    );

    private GreamReaperEnchantment() {
    }
}

