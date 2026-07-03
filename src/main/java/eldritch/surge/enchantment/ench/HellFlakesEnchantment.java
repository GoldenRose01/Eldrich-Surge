package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class HellFlakesEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "hell_flakes",
            "Hell Flakes",
            "ARMOR (PEZZI GENERICI MULTIPLI)",
            3,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/armor"),
            List.of("armor"),
            "#eldritch-surge:flakes",
            "Quando il giocatore va a fuoco o subisce calore, rigenera livello punti di durabilità al secondo."
    );

    private HellFlakesEnchantment() {
    }
}

