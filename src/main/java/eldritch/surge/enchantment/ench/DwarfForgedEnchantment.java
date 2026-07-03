package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class DwarfForgedEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "dwarf_forged",
            "Dwarf Forged",
            "ARMOR (PEZZI GENERICI MULTIPLI)",
            5,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/armor"),
            List.of("armor"),
            "#eldritch-surge:forged",
            "Applica un livello massiccio di Unbreaking (Unbreaking++) esclusivamente all'interno del bioma delle Caverne."
    );

    private DwarfForgedEnchantment() {
    }
}

