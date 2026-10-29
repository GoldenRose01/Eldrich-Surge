package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class FuseCutterEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "fuse_cutter", "Fuse Cutter", "SHIELDS", 1, "Rare", "Advanced",
            List.of("#eldrich-surge:enchantable/shield"), List.of("hand"), "", "Annulla le esplosioni entro 3 blocchi mentre pari con questo scudo.");

    private FuseCutterEnchantment() {}
}
