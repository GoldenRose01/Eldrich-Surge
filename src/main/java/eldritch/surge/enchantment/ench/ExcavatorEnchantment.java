package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class ExcavatorEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "excavator",
            "Excavator",
            "TOOLS (PICKAXES, SHOVELS, HOES, SHEARS)",
            3,
            "Rare",
            "Advanced",
            List.of("#minecraft:axes", "#minecraft:pickaxes", "#minecraft:shovels"),
            List.of("hand"),
            "",
            "Estende la distanza di interazione e scavo del giocatore (Reach++) simulando l'effetto della chela di granchio."
    );

    private ExcavatorEnchantment() {
    }
}

