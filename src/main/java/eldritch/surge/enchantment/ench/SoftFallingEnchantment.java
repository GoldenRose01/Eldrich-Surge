package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SoftFallingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "soft_falling",
            "Soft Falling",
            "BOOTS",
            1,
            "Unique",
            "Advanced",
            List.of("#minecraft:foot_armor"),
            List.of("feet"),
            "#eldritch-surge:falling",
            "Si sblocca fondendo l'incanto Feather Falling al livello 5: annulla del tutto i danni da caduta e rimuove l'effetto di caduta rallentata."
    );

    private SoftFallingEnchantment() {
    }
}

