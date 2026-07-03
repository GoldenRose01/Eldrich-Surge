package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class LaunchEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "launch",
            "Launch",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            5,
            "Epic",
            "Advanced",
            List.of("#minecraft:enchantable/trident"),
            List.of("hand"),
            "",
            "Incrementa sensibilmente la velocità di tiro e la stabilità del tridente quando viene scagliato."
    );

    private LaunchEnchantment() {
    }
}

