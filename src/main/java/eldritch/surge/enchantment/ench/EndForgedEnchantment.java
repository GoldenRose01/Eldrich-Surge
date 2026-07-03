package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class EndForgedEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "end_forged",
            "End Forged",
            "ARMOR (PEZZI GENERICI MULTIPLI)",
            5,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/armor"),
            List.of("armor"),
            "#eldritch-surge:forged",
            "Garantisce Unbreaking++ permanente sull'armatura finché ci si trova nella dimensione dell'End."
    );

    private EndForgedEnchantment() {
    }
}

