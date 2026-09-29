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
            "Incantesimo composito: Witch Hunter + Creeping Threat + Flogging + Herbicide + Smoother."
    );

    private StarFateEnchantment() {
    }
}

