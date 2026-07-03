package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class PaybackEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "payback",
            "Payback",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            5,
            "Epic",
            "Advanced",
            List.of("#minecraft:axes"),
            List.of("hand"),
            "",
            "Più la salute del giocatore è bassa, più il danno inflitto dall'ascia aumenta in modo esponenziale."
    );

    private PaybackEnchantment() {
    }
}

