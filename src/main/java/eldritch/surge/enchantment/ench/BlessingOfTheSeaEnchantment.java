package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class BlessingOfTheSeaEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "blessing_of_the_sea", "Blessing of the Sea", "ARMOR", 1, "Rare", "Advanced",
            List.of("#minecraft:enchantable/armor"), List.of("armor"), "", "Ogni 3 secondi in acqua o sotto la pioggia cura 1/8 di cuore e ripara di 1 punto ogni pezzo di armatura incantato.");

    private BlessingOfTheSeaEnchantment() {}
}
