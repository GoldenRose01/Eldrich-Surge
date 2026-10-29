package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CurseOfIncompatibilityEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "curse_of_incompatibility", "Curse of Incompatibility", "ALL", 1, "Common", "Normal",
            List.of("#minecraft:enchantable/durability"), List.of("any"), "", "Incompatibile con qualsiasi altro incantesimo.");

    private CurseOfIncompatibilityEnchantment() {}
}
