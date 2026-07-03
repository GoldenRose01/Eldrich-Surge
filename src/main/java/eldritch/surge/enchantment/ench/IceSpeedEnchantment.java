package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class IceSpeedEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "ice_speed",
            "Ice Speed",
            "BOOTS",
            5,
            "Epic",
            "Normal",
            List.of("#minecraft:foot_armor"),
            List.of("feet"),
            "",
            "Fornisce un aumento di velocità sui blocchi freddi (neve, strati di ghiaccio, ghiaccio compatto/azzurro e prismini) simile a Soul Speed."
    );

    private IceSpeedEnchantment() {
    }
}

