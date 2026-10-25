package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class BloodyMaryEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "bloody_mary", "Bloody Mary", "WEAPONS (SWORDS, AXES, HOES)",
            1, "Rare", "Advanced",
            List.of("#minecraft:swords", "#minecraft:axes", "#minecraft:hoes"),
            List.of("mainhand"), "",
            "10% di probabilità base, aumentata del 10% per ogni livello di Saccheggio."
    );
    private BloodyMaryEnchantment() {}
}
