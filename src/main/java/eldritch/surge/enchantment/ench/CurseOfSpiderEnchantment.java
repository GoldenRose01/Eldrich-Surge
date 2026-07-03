package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CurseOfSpiderEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "curse_of_spider",
            "Curse of Spider",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            5,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/weapon"),
            List.of("hand"),
            "#eldritch-surge:positive_curses",
            "Avvelena il bersaglio colpito. Sinergizza con la Gamerule lethalPoison."
    );

    private CurseOfSpiderEnchantment() {
    }
}

