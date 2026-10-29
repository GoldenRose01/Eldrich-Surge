package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class NeptunesWillEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "neptunes_will",
            "Neptune's Will",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            3,
            "Legendary",
            "Advanced",
            List.of("#minecraft:enchantable/trident"),
            List.of("hand"),
            "",
            "Aggiunge 5 danni per livello al tridente, anche lanciato; in mischia il bonus si applica anche alle altre armi se il tridente incantato è nella mano secondaria."
    );

    public static float bonusDamage(int level) {
        return 5.0F * Math.max(0, level);
    }

    private NeptunesWillEnchantment() {
    }
}

