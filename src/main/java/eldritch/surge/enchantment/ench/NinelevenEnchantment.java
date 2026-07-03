package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class NinelevenEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "nineleven",
            "Nineleven",
            "BOWS & CROSSBOWS",
            1,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/bow", "#minecraft:enchantable/crossbow"),
            List.of("hand"),
            "",
            "Abbate all'istante (Instakill) Phantom e Pipistrelli colpiti dal dardo."
    );

    private NinelevenEnchantment() {
    }
}

