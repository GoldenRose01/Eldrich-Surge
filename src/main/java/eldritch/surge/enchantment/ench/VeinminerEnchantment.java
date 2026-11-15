package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class VeinminerEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "veinminer",
            "Veinminer",
            "PICKAXES",
            1,
            "Rare",
            "Normal",
            List.of("#minecraft:pickaxes"),
            List.of("hand"),
            "",
            "Da accovacciato, rompe automaticamente tutti i minerali connessi della stessa vena (fino a 64 blocchi)."
    );

    private VeinminerEnchantment() {
    }
}
