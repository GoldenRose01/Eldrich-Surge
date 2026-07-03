package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class InkingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "inking",
            "Inking",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            5,
            "Common",
            "Normal",
            List.of("#minecraft:enchantable/trident"),
            List.of("hand"),
            "",
            "Acceca il bersaglio (Darkness) evidenziando la sua sagoma (Glowing) e debilitandolo (Weakness)."
    );

    private InkingEnchantment() {
    }
}

