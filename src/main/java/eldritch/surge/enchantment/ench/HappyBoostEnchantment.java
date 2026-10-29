package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HappyBoostEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "happy_boost", "Happy Boost", "ELYTRA", 3, "Rare", "Advanced",
            List.of("minecraft:elytra"), List.of("chest"), "", "Aumenta la velocità di volo con elitre del 50% / 80% / 110%.");

    private HappyBoostEnchantment() {}
}
