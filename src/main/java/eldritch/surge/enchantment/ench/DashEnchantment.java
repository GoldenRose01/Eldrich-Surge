package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class DashEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "dash",
            "Dash",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            5,
            "Uncommon",
            "Normal",
            List.of("#minecraft:swords"),
            List.of("hand"),
            "",
            "Aumenta il danno del colpo di 1 + livello/2 se la stessa arma con lo stesso incantesimo è impugnata contemporaneamente in entrambe le mani."
    );

    private DashEnchantment() {
    }
}

