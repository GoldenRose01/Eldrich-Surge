package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SmeltingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "smelting",
            "Smelting",
            "TOOLS (PICKAXES, SHOVELS, HOES, SHEARS)",
            1,
            "Legendary",
            "Advanced",
            List.of("#minecraft:axes", "#minecraft:pickaxes"),
            List.of("hand"),
            "",
            "Cuoce automaticamente i blocchi minerali estratti facendoli droppare sotto forma di lingotto/prodotto finito."
    );

    private SmeltingEnchantment() {
    }
}

