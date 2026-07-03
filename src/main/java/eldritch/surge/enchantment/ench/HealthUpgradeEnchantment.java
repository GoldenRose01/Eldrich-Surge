package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HealthUpgradeEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "health_upgrade",
            "Health Upgrade",
            "CHESTPLATE",
            5,
            "Epic",
            "Advanced",
            List.of("#minecraft:chest_armor"),
            List.of("chest"),
            "",
            "Aumenta la salute massima del giocatore (fornisce 1 riga intera di cuori aggiuntivi al livello 5)."
    );

    private HealthUpgradeEnchantment() {
    }
}

