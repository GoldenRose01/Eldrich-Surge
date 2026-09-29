package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class GodProtectionEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "god_protection", "God's Protection", "ARMOR AND TRIDENT", 1, "Unique", "Loot",
            List.of("#minecraft:enchantable/armor", "#minecraft:enchantable/trident"),
            List.of("head", "chest", "legs", "feet", "hand", "offhand"), "protection",
            "Riduce del 60% qualsiasi danno subito.");
    private GodProtectionEnchantment() {}
}
