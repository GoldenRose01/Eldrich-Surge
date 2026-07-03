package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class FreezeAspectEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "freeze_aspect",
            "Freeze Aspect",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            5,
            "Rare",
            "Normal",
            List.of("#minecraft:swords", "#minecraft:axes"),
            List.of("hand"),
            "",
            "Applica l'effetto rallentamento e congelamento progressivo sul bersaglio."
    );

    private FreezeAspectEnchantment() {
    }
}

