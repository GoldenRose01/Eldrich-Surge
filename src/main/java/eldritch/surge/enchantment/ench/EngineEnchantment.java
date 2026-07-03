package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class EngineEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "engine",
            "Engine",
            "ELYTRA",
            10,
            "Epic",
            "Advanced",
            List.of("minecraft:elytra"),
            List.of("chest"),
            "",
            "Ha una probabilità del 10% * livello di restituire il razzo pirotecnico quando viene utilizzato per accelerare in volo."
    );

    private EngineEnchantment() {
    }
}

