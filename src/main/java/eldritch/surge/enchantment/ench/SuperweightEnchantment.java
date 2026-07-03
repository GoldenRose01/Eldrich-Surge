package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SuperweightEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "superweight",
            "Superweight",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            5,
            "Rare",
            "Normal",
            List.of("#minecraft:swords"),
            List.of("hand"),
            "",
            "Rallenta la velocità d'attacco dell'arma ma incrementa massicciamente il danno base di ogni singolo colpo sferrato."
    );

    private SuperweightEnchantment() {
    }
}

