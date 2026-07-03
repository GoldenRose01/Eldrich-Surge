package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HealingAuraEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "healing_aura",
            "Healing Aura",
            "LEGGINGS",
            3,
            "Epic",
            "Advanced",
            List.of("#minecraft:leg_armor"),
            List.of("legs"),
            "",
            "Cura costantemente i giocatori alleati nelle strette vicinanze quando il portatore cammina accovacciato (Sneak)."
    );

    private HealingAuraEnchantment() {
    }
}

