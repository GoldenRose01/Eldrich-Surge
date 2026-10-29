package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CurseOfSlipperyEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "curse_of_slippery", "Curse of Slippery", "TOOLS AND WEAPONS", 1, "Common", "Normal",
            List.of("#eldrich-surge:enchantable/slippery"), List.of("hand"), "", "Fa cadere casualmente l’oggetto impugnato, in media ogni 33 secondi.");

    private CurseOfSlipperyEnchantment() {}
}
