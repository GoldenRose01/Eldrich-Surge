package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HeartOfTheSkyEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "heart_of_the_sky",
            "Heart of the Sky",
            "CHESTPLATE",
            1,
            "Legendary",
            "Advanced",
            List.of("#minecraft:chest_armor"),
            List.of("chest"),
            "#eldritch-surge:hearts",
            "Quando colpito, congela l'attaccante applicandogli l'effetto Frost (rallentamento e danno da freddo)."
    );

    private HeartOfTheSkyEnchantment() {
    }
}

