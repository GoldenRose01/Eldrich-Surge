package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class NeptunesWillEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "neptunes_will",
            "Neptune's Will",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            3,
            "Legendary",
            "Advanced",
            List.of("#minecraft:enchantable/trident"),
            List.of("hand"),
            "",
            "Aggiunge punti di attacco base diretti alle proprietà fisiche del tridente."
    );

    private NeptunesWillEnchantment() {
    }
}

