package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class DodgeEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "dodge", "Dodge", "LEGGINGS", 3, "Rare", "Advanced",
            List.of("#minecraft:enchantable/leg_armor"), List.of("legs"), "", "Evita completamente il danno con probabilità del 12,5% / 25% / 33%.");

    private DodgeEnchantment() {}
}
