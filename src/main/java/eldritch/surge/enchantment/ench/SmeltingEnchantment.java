package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SmeltingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "smelting",
            "Smelting",
            "TOOLS (AXES, PICKAXES, SHOVELS)",
            1,
            "Legendary",
            "Advanced",
            List.of("#minecraft:axes", "#minecraft:pickaxes", "#minecraft:shovels"),
            List.of("hand"),
            "",
            "Fonde i drop dei blocchi come in una fornace; funziona anche con le pale e trasforma le polveri di calcestruzzo in vetro colorato."
    );

    private SmeltingEnchantment() {
    }
}

