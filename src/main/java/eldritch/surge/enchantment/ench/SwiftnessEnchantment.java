package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SwiftnessEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "swiftness", "Swiftness", "BOOTS, BOW, CROSSBOW", 5, "Legendary", "Advanced",
            List.of("#minecraft:foot_armor", "#minecraft:enchantable/bow", "#minecraft:enchantable/crossbow"),
            List.of("feet", "hand", "offhand"), "",
            "Aumenta di 0,03 per livello la velocità di movimento a terra.");
    private SwiftnessEnchantment() {}
}
