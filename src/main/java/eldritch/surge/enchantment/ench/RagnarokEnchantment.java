package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class RagnarokEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "ragnarok",
            "Ragnarok",
            "CAST SPELLS (RITUALI AUTOMATICI)",
            1,
            "Ritual",
            "Advanced",
            List.of("minecraft:book"),
            List.of("hand"),
            "#eldritch-surge:rituals",
            "Evoca sul posto 4 Wither Skeleton dotati di scheletro equino come cavalcatura e armatura completa in Netherite."
    );

    private RagnarokEnchantment() {
    }
}

