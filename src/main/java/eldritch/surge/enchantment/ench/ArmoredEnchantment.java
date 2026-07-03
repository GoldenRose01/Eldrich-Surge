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
            "Fornisce punti armatura extra mentre si indossa l'elytra. Al livello 5 equivale a una piastra pettorale in Netherite."
    );

    private ArmoredEnchantment() {
    }
}

