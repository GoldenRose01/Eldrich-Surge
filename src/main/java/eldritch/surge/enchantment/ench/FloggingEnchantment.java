package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class FloggingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "flogging",
            "Flogging",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            7,
            "Uncommon",
            "Normal",
            List.of("#minecraft:enchantable/weapon"),
            List.of("hand"),
            "#eldritch-surge:additional_damage",
            "Danno maggiorato contro le entità contrassegnate nel tag della categoria 'Rebel'."
    );

    private FloggingEnchantment() {
    }
}

