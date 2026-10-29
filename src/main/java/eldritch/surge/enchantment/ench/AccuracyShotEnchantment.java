package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class AccuracyShotEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "accuracy_shot", "Accuracy Shot", "BOWS", 1, "Rare", "Normal Advanced",
            List.of("#minecraft:enchantable/bow"), List.of("hand"), "", "Le frecce seguono la mira senza dispersione o gravità e scompaiono dopo 30 secondi.");

    private AccuracyShotEnchantment() {}
}
