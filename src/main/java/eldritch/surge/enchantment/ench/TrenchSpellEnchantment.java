package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class TrenchSpellEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "trench_spell",
            "Trench Spell",
            "CAST SPELLS (RITUALI AUTOMATICI)",
            10,
            "Ritual",
            "Advanced",
            List.of("minecraft:book"),
            List.of("hand"),
            "#eldritch-surge:rituals",
            "Scava una voragine quadrata partendo dalla coordinata Y d'impatto scendendo in verticale fino a Y=-55. L'area d'aria ripulita segue la formula geometrica da -(2+livello) a +(2+livello)."
    );

    private TrenchSpellEnchantment() {
    }
}

