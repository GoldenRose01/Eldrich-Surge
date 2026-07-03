package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class MidasTouchEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "midas_touch",
            "Mida's Touch",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            5,
            "Rare",
            "Normal",
            List.of("#minecraft:swords", "#minecraft:hoes", "minecraft:netherite_hoe"),
            List.of("hand"),
            "",
            "Funziona come Looting, ma aggiunge la possibilità che i mob uccisi rilascino pepite o lingotti d'oro."
    );

    private MidasTouchEnchantment() {
    }
}

