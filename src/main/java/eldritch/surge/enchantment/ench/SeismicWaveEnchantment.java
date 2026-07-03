package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SeismicWaveEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "seismic_wave",
            "Seismic Wave",
            "MACE (NATIVO VANILLA)",
            3,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/mace"),
            List.of("hand"),
            "#eldritch-surge:weapon_utility",
            "Gli attacchi in caduta (Smash) generano un'onda d'urto che infligge il 20% * livello del danno totale ai mob nel raggio di 4 blocchi."
    );

    private SeismicWaveEnchantment() {
    }
}
