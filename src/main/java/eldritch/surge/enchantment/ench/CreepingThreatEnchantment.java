package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CreepingThreatEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "creeping_threat",
            "Creeping Threat",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            7,
            "Uncommon",
            "Normal",
            List.of("#minecraft:enchantable/weapon"),
            List.of("hand"),
            "#eldritch-surge:additional_damage",
            "Variante potenziata di Bane of Arthropods estesa a tutte le classi di artropodi custom."
    );

    private CreepingThreatEnchantment() {
    }
}

