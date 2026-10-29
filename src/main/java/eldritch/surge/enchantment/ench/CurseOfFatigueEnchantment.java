package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class CurseOfFatigueEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "curse_of_fatigue", "Curse of Fatigue", "TOOLS, WEAPONS AND ARMOR", 3, "Common", "Normal",
            List.of("#minecraft:enchantable/durability"), List.of("any"), "", "Scavo, attacco, carica e animazione: -20% / -36% / -48,8%. Armatura: velocità moltiplicata per 0,95 per livello, cumulativa tra pezzi (fino a -46%). Incompatibile con Efficienza.");

    private CurseOfFatigueEnchantment() {}
}
