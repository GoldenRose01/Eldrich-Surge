package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class SeekerBlessingEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "seeker_blessing", "Seeker Blessing", "HELMET", 5, "Legendary", "Advanced",
            List.of("#minecraft:head_armor"), List.of("head"), "blessings",
            "Rende luminosi gli oggetti a terra entro 2 blocchi per livello.");
    private SeekerBlessingEnchantment() {}
}
