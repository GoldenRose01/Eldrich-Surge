package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class BlessingOfTheNightEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "blessing_of_the_night",
            "Blessing of the Night",
            "HELMET",
            5,
            "Legendary",
            "Advanced",
            List.of("#minecraft:head_armor"),
            List.of("head"),
            "#eldritch-surge:blessings",
            "Funziona come l'incanto del sole, ma genera XP solo durante la notte senza necessità di esposizione diretta al cielo."
    );

    private BlessingOfTheNightEnchantment() {
    }
}

