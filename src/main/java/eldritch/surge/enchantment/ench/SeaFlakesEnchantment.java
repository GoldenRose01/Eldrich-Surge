package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SeaFlakesEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "sea_flakes",
            "Sea Flakes",
            "ARMOR (PEZZI GENERICI MULTIPLI)",
            3,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/armor"),
            List.of("armor"),
            "#eldritch-surge:flakes",
            "Rigenera 1 punto di durabilità ogni 4/livello minuti quando ci si trova sotto la pioggia o immersi in acqua."
    );

    private SeaFlakesEnchantment() {
    }
}

