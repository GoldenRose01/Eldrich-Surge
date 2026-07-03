package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CurseOfTheForestEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "curse_of_the_forest",
            "Curse of the Forest",
            "ALL (APPLICABILI A QUALSIASI OGGETTO)",
            2,
            "Epic",
            "Advanced",
            List.of("#minecraft:enchantable/durability"),
            List.of("hand"),
            "#eldritch-surge:negative_curses",
            "Alla morte del giocatore, l'oggetto genera un'esplosione controllata (Lvl 1 = Esplosione Creeper, Lvl 2 = Creeper Caricato)."
    );

    private CurseOfTheForestEnchantment() {
    }
}

