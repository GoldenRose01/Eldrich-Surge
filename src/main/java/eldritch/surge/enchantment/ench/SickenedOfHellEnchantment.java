package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SickenedOfHellEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "sickened_of_hell",
            "Sickened of Hell",
            "TOOLS (PICKAXES, SHOVELS, HOES, SHEARS)",
            1,
            "Uncommon",
            "Normal",
            List.of("#minecraft:hoes"),
            List.of("hand"),
            "",
            "Qualsiasi raccolto agricolo distrutto tramite la zappa viene droppato già cotto."
    );

    private SickenedOfHellEnchantment() {
    }
}

