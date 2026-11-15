package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class MagneticEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "magnetic",
            "Magnetic",
            "ARMOR STAND",
            1,
            "Rare",
            "Normal",
            List.of("minecraft:armor_stand"),
            List.of("any"),
            "",
            "Attira gli oggetti a terra entro 8 blocchi di raggio verso il centro dell'Armor Stand."
    );

    private MagneticEnchantment() {
    }
}
