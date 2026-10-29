package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class KineticProtectionEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "kinetic_protection", "Kinetic Protection", "ELYTRA", 5, "Rare", "Advanced",
            List.of("minecraft:elytra"), List.of("chest"), "", "Durante il volo aggiunge 6 punti armatura per livello (30 al V). Riduce i danni da impatto contro pareti del 24% per livello, fino all’80%.");

    private KineticProtectionEnchantment() {}
}
