package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class LeechingAspectEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "leeching_aspect",
            "Leeching Aspect",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            2,
            "Rare",
            "Normal",
            List.of("#minecraft:swords", "#minecraft:axes"),
            List.of("hand"),
            "",
            "Se metti a segno un attacco critico, converte parte del danno in cuori di assorbimento per il giocatore."
    );

    private LeechingAspectEnchantment() {
    }
}

