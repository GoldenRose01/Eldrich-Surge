package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class PiercingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "piercing",
            "Piercing",
            "BOWS & CROSSBOWS",
            10,
            "Epic",
            "Normal",
            List.of("#minecraft:enchantable/bow", "#minecraft:enchantable/crossbow"),
            List.of("hand"),
            "",
            "Estensione del livello di perforazione dei dardi fino al livello 10, permettendo di attraversare interi gruppi di mob."
    );

    private PiercingEnchantment() {
    }
}

