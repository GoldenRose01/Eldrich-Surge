package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HeartseekerEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "heartseeker",
            "Heartseeker",
            "SPEAR (NATIVA VANILLA)",
            1,
            "Legendary",
            "Advanced",
            List.of("#eldrich-surge:enchantable/spear"),
            List.of("hand"),
            "#eldrich-surge:aspect",
            "Assorbe e cura il player dei cuori inflitti al bersaglio durante un attacco in carica con la lancia."
    );

    private HeartseekerEnchantment() {
    }
}
