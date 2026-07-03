package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SeaBreezeEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "sea_breeze",
            "Sea Breeze",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            3,
            "Uncommon",
            "Normal",
            List.of("#minecraft:enchantable/trident"),
            List.of("hand"),
            "",
            "Il tridente lanciato rilascia una carica di vento (Wind Charge) che scaraventa via i nemici vicini all'impatto."
    );

    private SeaBreezeEnchantment() {
    }
}

