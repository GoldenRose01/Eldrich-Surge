package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class ElasticityEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "elasticity",
            "Elasticity",
            "BOWS & CROSSBOWS",
            3,
            "Legendary",
            "Advanced",
            List.of("#minecraft:enchantable/bow", "#minecraft:enchantable/crossbow"),
            List.of("hand"),
            "",
            "La velocità di uscita del proiettile aumenta linearmente basandosi sulla formula velocità * livello."
    );

    private ElasticityEnchantment() {
    }
}

