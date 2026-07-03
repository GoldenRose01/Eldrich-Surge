package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SmootherEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "smoother",
            "Smoother",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            7,
            "Uncommon",
            "Normal",
            List.of("#minecraft:enchantable/weapon"),
            List.of("hand"),
            "#eldritch-surge:additional_damage",
            "Modificatore di danno aumentato contro tutti i mob di forma cubica (es. Slime, Magma Cube)."
    );

    private SmootherEnchantment() {
    }
}

