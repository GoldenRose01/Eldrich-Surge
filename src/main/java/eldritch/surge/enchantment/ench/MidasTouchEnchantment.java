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
            "Ogni livello aggiunge il 20% di possibilità che una creatura uccisa lasci pepite o un lingotto d'oro."
    );

    private MidasTouchEnchantment() {
    }
}

