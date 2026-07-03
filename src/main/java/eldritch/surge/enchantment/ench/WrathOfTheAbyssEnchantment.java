package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class WrathOfTheAbyssEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "wrath_of_the_abyss",
            "Wrath of the Abyss",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            7,
            "Rare",
            "Normal",
            List.of("#minecraft:enchantable/weapon"),
            List.of("hand"),
            "#eldritch-surge:additional_damage",
            "Incrementa il danno in modo analogo a Impaling, ma esteso a tutte le entità acquatiche del gioco."
    );

    private WrathOfTheAbyssEnchantment() {
    }
}

