package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class ImpalerReachEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "impaler_reach",
            "Impaler Reach",
            "SPEAR (NATIVA VANILLA)",
            3,
            "Epic",
            "Advanced",
            List.of("#eldritch-surge:enchantable/spear"),
            List.of("hand"),
            "#eldritch-surge:weapon_utility",
            "Incrementa l'attributo nativo generic.player.reach_distance di +1 blocco per livello. Incompatibile con Sweeping Edge."
    );

    private ImpalerReachEnchantment() {
    }
}
