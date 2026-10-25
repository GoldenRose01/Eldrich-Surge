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
            "#eldrich-surge:falling",
            "Premi Salto mentre sei in aria per attivare Caduta Lenta per alcuni secondi."
    );

    private SoftFallingEnchantment() {
    }
}

