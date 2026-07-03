package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class EnduringEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "enduring",
            "Enduring",
            "ALL (APPLICABILI A QUALSIASI OGGETTO)",
            1,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/durability"),
            List.of("hand"),
            "",
            "Gli oggetti incantati rimangono persistenti a terra all'infinito quando vengono droppati, prevenendo il despawn dei 5 minuti."
    );

    private EnduringEnchantment() {
    }
}

