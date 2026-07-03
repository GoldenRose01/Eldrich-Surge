package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class StarFateEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "star_fate",
            "Star Fate",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            9,
            "Legendary",
            "Advanced",
            List.of("#minecraft:enchantable/weapon"),
            List.of("hand"),
            "#eldritch-surge:additional_damage",
            "Super incantesimo AIO che unisce Witch Hunter + Bane of Arthropods + Flogging + Herbicide + Smoother."
    );

    private StarFateEnchantment() {
    }
}

