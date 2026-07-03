package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class VoidSweepEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "void_sweep",
            "Void Sweep",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            5,
            "Rare",
            "Advanced",
            List.of("#minecraft:hoes", "minecraft:netherite_hoe"),
            List.of("hand"),
            "",
            "L'attacco ad area della zappa esegue un fendente esteso che applica 10 + livello danni fissi e Knockback II."
    );

    private VoidSweepEnchantment() {
    }
}

