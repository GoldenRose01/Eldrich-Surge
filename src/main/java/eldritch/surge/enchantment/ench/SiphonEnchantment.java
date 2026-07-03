package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SiphonEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "siphon",
            "Siphon",
            "SHULKER BOXES (AUTOMAZIONE INVENTARIO)",
            1,
            "Epic",
            "Normal",
            List.of("minecraft:shulker_box", "minecraft:white_shulker_box", "minecraft:orange_shulker_box", "minecraft:magenta_shulker_box", "minecraft:light_blue_shulker_box", "minecraft:yellow_shulker_box", "minecraft:lime_shulker_box", "minecraft:pink_shulker_box", "minecraft:gray_shulker_box", "minecraft:light_gray_shulker_box", "minecraft:cyan_shulker_box", "minecraft:purple_shulker_box", "minecraft:blue_shulker_box", "minecraft:brown_shulker_box", "minecraft:green_shulker_box", "minecraft:red_shulker_box", "minecraft:black_shulker_box"),
            List.of("hand"),
            "",
            "Gli oggetti raccolti da terra vengono inseriti direttamente in uno stack incompleto compatibile presente all'interno della Shulker Box."
    );

    private SiphonEnchantment() {
    }
}

