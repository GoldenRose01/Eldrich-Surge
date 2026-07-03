package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SniperEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "sniper",
            "Sniper",
            "BOWS & CROSSBOWS",
            1,
            "Uncommon",
            "Normal",
            List.of("#minecraft:enchantable/bow", "#minecraft:enchantable/crossbow"),
            List.of("hand"),
            "",
            "Il danno del proiettile aumenta proporzionalmente in base alla distanza coperta dal colpo prima dell'impatto."
    );

    private SniperEnchantment() {
    }
}

