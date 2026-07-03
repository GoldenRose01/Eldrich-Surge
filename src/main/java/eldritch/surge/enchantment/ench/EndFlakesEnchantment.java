package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class EndFlakesEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "end_flakes",
            "End Flakes",
            "ARMOR (PEZZI GENERICI MULTIPLI)",
            3,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/armor"),
            List.of("armor"),
            "#eldritch-surge:flakes",
            "Quando non si tocca il terreno, rigenera livello/100 durabilità al secondo."
    );

    private EndFlakesEnchantment() {
    }
}

