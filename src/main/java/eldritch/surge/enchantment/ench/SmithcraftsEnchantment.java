package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SmithcraftsEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "smithcrafts",
            "Smithcrafts",
            "SHIELDS",
            3,
            "Common",
            "Normal",
            List.of("minecraft:shield"),
            List.of("offhand"),
            "",
            "Fornisce 2 punti armatura per livello mentre lo scudo è equipaggiato nella mano secondaria."
    );

    private SmithcraftsEnchantment() {
    }
}

