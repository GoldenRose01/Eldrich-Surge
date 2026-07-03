package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class PhalanxStanceEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "phalanx_stance",
            "Phalanx Stance",
            "SPEAR (NATIVA VANILLA)",
            3,
            "Rare",
            "Advanced",
            List.of("#eldritch-surge:enchantable/spear"),
            List.of("hand"),
            "#eldritch-surge:offhand",
            "Se tenuta nell'offhand con uno scudo nella principale, conferisce +2 Armor e +1 Armor Toughness per livello (riduce la velocita del 10%)."
    );

    private PhalanxStanceEnchantment() {
    }
}
