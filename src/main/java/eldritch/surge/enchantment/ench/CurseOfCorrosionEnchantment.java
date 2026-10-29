package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CurseOfCorrosionEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "curse_of_corrosion", "Curse of Corrosion", "ARMOR", 1, "Common", "Normal",
            List.of("#minecraft:enchantable/armor"), List.of("armor"), "", "Infligge 1/8 di cuore di danno ogni 3 secondi in acqua o sotto la pioggia.");

    private CurseOfCorrosionEnchantment() {}
}
