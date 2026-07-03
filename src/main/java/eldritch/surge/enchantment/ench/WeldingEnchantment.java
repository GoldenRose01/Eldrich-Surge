package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class WeldingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "welding",
            "Welding",
            "ALL (APPLICABILI A QUALSIASI OGGETTO)",
            5,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/durability"),
            List.of("hand"),
            "",
            "Blocca e mantiene basso il costo di riparazione in livelli XP accumulato nell'interfaccia dell'incudine."
    );

    private WeldingEnchantment() {
    }
}

