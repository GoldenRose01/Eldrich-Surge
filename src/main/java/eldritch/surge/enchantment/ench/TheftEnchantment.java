package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class TheftEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "theft",
            "Theft",
            "BOWS & CROSSBOWS",
            5,
            "Legendary",
            "Advanced",
            List.of("#minecraft:enchantable/bow", "#minecraft:enchantable/crossbow"),
            List.of("hand"),
            "",
            "Trasferisce la proprietà Looting (Saccheggio) direttamente alle frecce scoccate dall'arco."
    );

    private TheftEnchantment() {
    }
}

