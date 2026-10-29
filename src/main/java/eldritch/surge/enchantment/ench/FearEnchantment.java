package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class FearEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "fear", "Fear", "SWORDS", 1, "Rare", "Normal Advanced",
            List.of("#minecraft:swords"), List.of("mainhand"), "", "Ogni colpo di spada riuscito aggiunge 1 secondo al timer di esplosione del creeper.");

    private FearEnchantment() {}
}
