package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CurseOfPerishEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "curse_of_perish",
            "Curse of Perish",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            3,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/weapon"),
            List.of("hand"),
            "#eldritch-surge:positive_curses",
            "Infligge l'effetto di stato Wither al target con intensità pari al livello dell'incanto."
    );

    private CurseOfPerishEnchantment() {
    }
}

