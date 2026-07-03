package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class VanguardChargeEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "vanguard_charge",
            "Vanguard Charge",
            "SPEAR (NATIVA VANILLA)",
            2,
            "Ultra",
            "Advanced",
            List.of("#eldritch-surge:enchantable/spear"),
            List.of("hand"),
            "#eldritch-surge:weapon_utility",
            "Doppia pressione dello scatto esegue un affondo aereo che trascina i mob colpiti fino al primo blocco solido, infliggendo danno da impatto."
    );

    private VanguardChargeEnchantment() {
    }
}
