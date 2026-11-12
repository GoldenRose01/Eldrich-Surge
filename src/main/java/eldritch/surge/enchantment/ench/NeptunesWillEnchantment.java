package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class NeptunesWillEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "neptunes_will",
            "Neptune's Will",
            "WEAPONS (TRIDENTS)",
            5,
            "Legendary",
            "Advanced",
            List.of("#minecraft:enchantable/trident"),
            List.of("hand"),
            "#eldrich-surge:additional_damage",
            "Sostituisce Impalamento: conferisce al tridente un incremento del danno d'attacco (+2.5 per livello) in mischia e al lancio."
    );

    public static float bonusDamage(int level) {
        return 2.5F * Math.max(0, level);
    }

    private NeptunesWillEnchantment() {
    }
}
