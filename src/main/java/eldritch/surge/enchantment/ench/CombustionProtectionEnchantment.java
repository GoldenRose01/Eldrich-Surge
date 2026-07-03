package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CombustionProtectionEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "combustion_protection",
            "Combustion Protection",
            "ARMOR (PEZZI GENERICI MULTIPLI)",
            5,
            "Epic",
            "Advanced",
            List.of("#minecraft:enchantable/armor"),
            List.of("armor"),
            "",
            "Fusione avanzata che fornisce contemporaneamente la protezione dalle esplosioni e la protezione dal fuoco."
    );

    private CombustionProtectionEnchantment() {
    }
}

