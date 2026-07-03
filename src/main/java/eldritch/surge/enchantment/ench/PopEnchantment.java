package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class PopEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "pop",
            "Pop",
            "BOWS & CROSSBOWS",
            5,
            "Epic",
            "Normal",
            List.of("#minecraft:enchantable/bow", "#minecraft:enchantable/crossbow"),
            List.of("hand"),
            "",
            "Converte la freccia scoccata in un fuoco d'artificio esplosivo il cui raggio e danno scalano in base al livello."
    );

    private PopEnchantment() {
    }
}

