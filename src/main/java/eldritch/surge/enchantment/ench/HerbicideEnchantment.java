package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HerbicideEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "herbicide",
            "Herbicide",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            7,
            "Uncommon",
            "Normal",
            List.of("#minecraft:enchantable/weapon"),
            List.of("hand"),
            "#eldritch-surge:additional_damage",
            "Danno incrementato contro i mob di tipo fungo (Fungi)."
    );

    private HerbicideEnchantment() {
    }
}

