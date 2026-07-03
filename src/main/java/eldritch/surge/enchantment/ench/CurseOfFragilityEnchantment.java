package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CurseOfFragilityEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "curse_of_fragility",
            "Curse of Fragility",
            "ALL (APPLICABILI A QUALSIASI OGGETTO)",
            3,
            "Common",
            "Normal",
            List.of("#minecraft:enchantable/durability"),
            List.of("hand"),
            "#eldritch-surge:negative_curses",
            "L'esatto opposto di Unbreaking; aumenta sensibilmente il consumo di durabilità dell'oggetto ad ogni utilizzo."
    );

    private CurseOfFragilityEnchantment() {
    }
}

