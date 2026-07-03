package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class StormSpellEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "storm_spell",
            "Storm Spell",
            "CAST SPELLS (RITUALI AUTOMATICI)",
            1,
            "Ritual",
            "Advanced",
            List.of("#minecraft:enchantable/durability"),
            List.of("hand"),
            "#eldritch-surge:rituals",
            "Forzza il meteo globale in temporale elettrico ed evoca 5 fulmini casuali nel raggio ristretto di 32 blocchi."
    );

    private StormSpellEnchantment() {
    }
}

