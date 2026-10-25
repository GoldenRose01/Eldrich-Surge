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
            "Colpo alle spalle. Attaccare un'entita non voltata verso il player garantisce un colpo critico automatico che ignora il 50% dell'armatura."
    );

    private HeartseekerEnchantment() {
    }
}
