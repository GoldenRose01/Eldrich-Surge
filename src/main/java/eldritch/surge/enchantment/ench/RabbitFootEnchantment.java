package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class RabbitFootEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "rabbit_foot", "Rabbit Foot", "ARMOR", 5, "Ultra", "Advanced",
            List.of("#minecraft:head_armor", "#minecraft:foot_armor"), List.of("head", "feet"), "",
            "Aumenta la fortuna dell'utilizzatore di 1 punto per livello.");
    private RabbitFootEnchantment() {}
}
