package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class DualSweepingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "dual_sweeping",
            "Dual Sweeping",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            2,
            "Legendary",
            "Advanced",
            List.of("#minecraft:swords"),
            List.of("hand"),
            "",
            "Il danno generato dall'attacco a spazzata viene duplicato rispetto al danno base dell'arma principale."
    );

    private DualSweepingEnchantment() {
    }
}

