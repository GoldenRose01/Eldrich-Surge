package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class LungeEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "lunge",
            "Lunge",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            1,
            "Ultra",
            "Advanced",
            List.of("#minecraft:swords"),
            List.of("hand"),
            "",
            "Genera Knockback V sul bersaglio e ne frantuma parzialmente i punti armatura per quel singolo colpo."
    );

    private LungeEnchantment() {
    }
}

