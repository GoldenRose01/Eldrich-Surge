package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class GreamReaperEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "gream_reaper",
            "Gream Reaper",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            10,
            "Unique",
            "Normal",
            List.of("#minecraft:hoes", "minecraft:netherite_hoe"),
            List.of("hand"),
            "",
            "Sbloccabile unendo Blade of Apocalypse + Star Fate. Aggiunge un bonus di livello cumulativo."
    );

    private GreamReaperEnchantment() {
    }
}

