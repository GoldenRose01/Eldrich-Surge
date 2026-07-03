package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HeavyImpactEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "heavy_impact",
            "Heavy Impact",
            "MACE (NATIVO VANILLA)",
            5,
            "Rare",
            "Advanced",
            List.of("#minecraft:enchantable/mace"),
            List.of("hand"),
            "#eldritch-surge:additional_damage",
            "Incrementa il danno base d'impatto standard della mazza a terra. Incompatibile con i danni vanilla."
    );

    private HeavyImpactEnchantment() {
    }
}
