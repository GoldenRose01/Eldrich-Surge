package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class DiggerEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "digger",
            "Digger",
            "TOOLS (AXES, PICKAXES, SHOVELS)",
            1,
            "Rare",
            "Normal, Advanced",
            List.of("#minecraft:axes", "#minecraft:pickaxes", "#minecraft:shovels"),
            List.of("hand"),
            "",
            "Scaviamo un po' più largo."
    );

    private DiggerEnchantment() {
    }
}

