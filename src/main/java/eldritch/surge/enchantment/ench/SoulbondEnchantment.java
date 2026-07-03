package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SoulbondEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "soulbond",
            "Soulbond",
            "ALL (APPLICABILI A QUALSIASI OGGETTO)",
            1,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/durability"),
            List.of("hand"),
            "",
            "Mantiene l'oggetto nell'inventario del giocatore dopo la morte anche se la gamerule keepInventory è impostata su false."
    );

    private SoulbondEnchantment() {
    }
}

