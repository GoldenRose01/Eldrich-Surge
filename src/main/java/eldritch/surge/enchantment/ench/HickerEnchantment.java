package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HickerEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "hicker",
            "Hicker",
            "LEGGINGS",
            5,
            "Epic",
            "Normal",
            List.of("#minecraft:leg_armor"),
            List.of("legs"),
            "",
            "Permette al giocatore di saltare più in alto di 1 + livello/5 blocchi (simile al salto assistito dei cavalli)."
    );

    private HickerEnchantment() {
    }
}

