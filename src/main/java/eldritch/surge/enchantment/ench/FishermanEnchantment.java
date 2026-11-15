package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class FishermanEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "fisherman",
            "Fisherman",
            "FISHING ROD",
            1,
            "Legendary",
            "Advanced",
            List.of("#minecraft:enchantable/fishing"),
            List.of("hand"),
            "",
            "Garantisce che un pesce abbocchi sempre entro 5 secondi."
    );

    private FishermanEnchantment() {
    }
}
