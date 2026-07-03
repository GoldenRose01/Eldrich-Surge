package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class PruningEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "pruning",
            "Pruning",
            "TOOLS (PICKAXES, SHOVELS, HOES, SHEARS)",
            1,
            "Uncommon",
            "Normal",
            List.of("minecraft:shears"),
            List.of("hand"),
            "",
            "Distrugge istantaneamente (Instamine) blocchi di foglie, erba e altri blocchi delicati gestiti dalle cesoie."
    );

    private PruningEnchantment() {
    }
}

