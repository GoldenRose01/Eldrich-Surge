package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class ArmoredEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "armored",
            "Armored",
            "ELYTRA",
            5,
            "Common",
            "Normal",
            List.of("minecraft:elytra"),
            List.of("chest"),
            "",
            "Aggiunge 2 punti armatura per livello mentre si indossa l'Elytra, fino a 10 punti al livello V."
    );

    private ArmoredEnchantment() {
    }
}

