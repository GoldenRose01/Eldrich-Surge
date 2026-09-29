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
            "Sbloccabile fondendo Blade of Apocalypse + Star Fate oppure potenziando Katana con una Nether Star. Aggiunge un bonus di livello cumulativo."
    );

    private GreamReaperEnchantment() {
    }
}

