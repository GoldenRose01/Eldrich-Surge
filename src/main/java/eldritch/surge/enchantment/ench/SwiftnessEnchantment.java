package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SwiftnessEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "swiftness", "Swiftness", "BOW, SPEAR", 5, "Legendary", "Advanced",
            List.of("#minecraft:enchantable/bow", "#minecraft:spears"),
            List.of("hand", "offhand"), "",
            "Aumenta la velocità di movimento di 0,1 per livello durante la carica di arco o lancia.");
    private SwiftnessEnchantment() {}
}
