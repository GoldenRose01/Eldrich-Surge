package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class FanaticEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "fanatic", "Fanatic", "FISHING RODS", 1, "Rare", "Normal Advanced",
            List.of("#minecraft:enchantable/fishing"), List.of("hand"), "", "Ogni cattura completata aggiunge 1 punto percentuale alla probabilità di tesoro della canna, fino al 100%.");

    private FanaticEnchantment() {}
}
