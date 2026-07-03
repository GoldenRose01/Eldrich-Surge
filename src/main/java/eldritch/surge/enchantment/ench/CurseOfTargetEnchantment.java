package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CurseOfTargetEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "curse_of_target",
            "Curse of Target",
            "BOWS & CROSSBOWS",
            5,
            "Uncommon",
            "Normal",
            List.of("#minecraft:enchantable/bow", "#minecraft:enchantable/crossbow"),
            List.of("hand"),
            "#eldritch-surge:positive_curses",
            "Le frecce andate a segno applicano lo stato Splendente (Glowing) al bersaglio per un numero di secondi pari a livello."
    );

    private CurseOfTargetEnchantment() {
    }
}

