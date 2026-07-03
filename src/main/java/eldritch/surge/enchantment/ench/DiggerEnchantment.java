package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class DiggerEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "digger",
            "Digger",
            "TOOLS (PICKAXES, SHOVELS, HOES, SHEARS)",
            1,
            "Rare",
            "Normal, Advanced",
            List.of("#minecraft:axes", "#minecraft:pickaxes", "#minecraft:shovels"),
            List.of("hand"),
            "",
            "Rompe i blocchi in un'area quadrata allargata di 3x3 incentrata sul blocco mirato."
    );

    private DiggerEnchantment() {
    }
}

