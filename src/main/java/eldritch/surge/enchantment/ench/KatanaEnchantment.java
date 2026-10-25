package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class KatanaEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "katana",
            "Katana",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            10,
            "Common",
            "Normal",
            List.of("#minecraft:enchantable/weapon"),
            List.of("hand"),
            "#eldrich-surge:additional_damage",
            "Rende la lama ultratagliente."
    );

    private KatanaEnchantment() {
    }
}

