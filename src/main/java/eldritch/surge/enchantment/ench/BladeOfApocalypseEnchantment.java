package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class BladeOfApocalypseEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "blade_of_apocalypse",
            "Blade of Apocalypse",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            9,
            "Legendary",
            "Advanced",
            List.of("#minecraft:enchantable/weapon"),
            List.of("hand"),
            "#eldritch-surge:additional_damage",
            "Incantesimo definitivo AIO. Combina Butcher + Undead Slayer + Bane of End + Exorcist + Wrath of the Abyss."
    );

    private BladeOfApocalypseEnchantment() {
    }
}

