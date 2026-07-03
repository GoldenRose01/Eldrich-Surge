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
            List.of("hand"),
            "",
            "Fornisce punti armatura extra stabili quando lo scudo viene equipaggiato stabilmente nella mano secondaria (Offhand)."
    );

    private SmithcraftsEnchantment() {
    }
}

