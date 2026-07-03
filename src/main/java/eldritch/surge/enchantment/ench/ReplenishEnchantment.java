package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class ReplenishEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "replenish",
            "Replenish",
            "BOWS & CROSSBOWS",
            3,
            "Rare",
            "Normal",
            List.of("#minecraft:enchantable/bow", "#minecraft:enchantable/crossbow"),
            List.of("hand"),
            "",
            "Fornisce una probabilità del 33% * livello di non consumare proiettili speciali (frecce con effetti)."
    );

    private ReplenishEnchantment() {
    }
}

