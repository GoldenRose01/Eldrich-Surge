package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class GravityWellEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "gravity_well",
            "Gravity Well",
            "MACE (NATIVO VANILLA)",
            2,
            "Epic",
            "Advanced",
            List.of("#minecraft:enchantable/mace"),
            List.of("hand"),
            "#eldritch-surge:weapon_utility",
            "Durante un attacco dall'alto, attira lentamente i mob nel raggio di 5 blocchi verso il punto d'impatto."
    );

    private GravityWellEnchantment() {
    }
}
