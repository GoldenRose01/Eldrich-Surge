package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class ButcherEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "butcher",
            "Butcher",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            7,
            "Uncommon",
            "Normal",
            List.of("#minecraft:enchantable/weapon"),
            List.of("hand"),
            "#eldritch-surge:additional_damage",
            "Aumento del danno specifico contro gli animali della fattoria."
    );

    private ButcherEnchantment() {
    }
}

