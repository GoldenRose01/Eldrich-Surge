package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class DwarfFlakesEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "dwarf_flakes",
            "Dwarf Flakes",
            "ARMOR (PEZZI GENERICI MULTIPLI)",
            3,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/armor"),
            List.of("armor"),
            "#eldritch-surge:flakes",
            "Quando il livello di luce ambientale è inferiore a 8, ripara 1 punto di durabilità dell'armatura ogni 4/livello minuti."
    );

    private DwarfFlakesEnchantment() {
    }
}

