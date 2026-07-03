package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CocktailSpellEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "cocktail_spell",
            "Cocktail Spell",
            "CAST SPELLS (RITUALI AUTOMATICI)",
            1,
            "Ritual",
            "Advanced",
            List.of("#minecraft:enchantable/durability"),
            List.of("hand"),
            "#eldritch-surge:rituals",
            "Propaga istantaneamente tutti gli effetti di stato attivi sull'utilizzatore a tutti i player nel raggio d'azione con intensità X."
    );

    private CocktailSpellEnchantment() {
    }
}

