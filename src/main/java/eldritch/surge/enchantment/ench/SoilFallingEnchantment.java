package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SoilFallingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "soil_falling",
            "Soil Falling",
            "BOOTS",
            1,
            "Rare",
            "Normal",
            List.of("#minecraft:foot_armor"),
            List.of("feet"),
            "#eldritch-surge:falling",
            "Impedisce la distruzione dei blocchi di terreno zappato (farmland/crops) quando ci si salta sopra."
    );

    private SoilFallingEnchantment() {
    }
}

