package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class ExperiencedEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "experienced",
            "Experienced",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            1,
            "Ultra",
            "Advanced",
            List.of("#minecraft:swords", "#minecraft:axes"),
            List.of("hand"),
            "",
            "Incrementa drasticamente il moltiplicatore di danno applicato esclusivamente ai colpi critici."
    );

    private ExperiencedEnchantment() {
    }
}

