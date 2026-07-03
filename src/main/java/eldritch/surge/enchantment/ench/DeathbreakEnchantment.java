package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class DeathbreakEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "deathbreak",
            "DeathBreak",
            "ALL (APPLICABILI A QUALSIASI OGGETTO)",
            1,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/durability"),
            List.of("hand"),
            "",
            "Rende l'oggetto completamente indistruttibile bloccando la durabilità, ma sovrascrive e disabilita Unbreaking."
    );

    private DeathbreakEnchantment() {
    }
}

