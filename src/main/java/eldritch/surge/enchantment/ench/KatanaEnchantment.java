package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class KatanaEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "katana",
            "Katana",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            10,
            "Common",
            "Normal",
            List.of("#minecraft:enchantable/weapon"),
            List.of("hand"),
            "#eldritch-surge:additional_damage",
            "Funziona come Sharpness ma applica uno scaling di danno base per livello superiore."
    );

    private KatanaEnchantment() {
    }
}

