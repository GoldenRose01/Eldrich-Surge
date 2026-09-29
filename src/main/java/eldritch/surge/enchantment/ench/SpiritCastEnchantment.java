package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SpiritCastEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "spirti_cast", "Spirit Cast", "WEAPONS", 10, "Uncommon", "Normal",
            List.of("#minecraft:enchantable/weapon", "minecraft:netherite_hoe"), List.of("hand"), "power_up",
            "Ogni 15 meno livello uccisioni assegna casualmente Forza, Velocità o Resistenza per 10 secondi.");
    private SpiritCastEnchantment() {}
}
