package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class ImmortalityEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "immortality", "Immortality", "SHIELDS", 1, "Ultra", "Advanced",
            List.of("#eldrich-surge:enchantable/shield"), List.of("hand"), "", "Ti salva dalla morte mentre impugni lo scudo; consuma solo questo incantesimo e ripristina 1 cuore.");

    private ImmortalityEnchantment() {}
}
