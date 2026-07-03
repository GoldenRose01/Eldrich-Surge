package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class WeaponProtectionEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "weapon_protection",
            "Weapon Protection",
            "ARMOR (PEZZI GENERICI MULTIPLI)",
            5,
            "Rare",
            "Normal",
            List.of("#minecraft:enchantable/armor"),
            List.of("armor"),
            "",
            "Fornisce una riduzione del danno specifica contro gli attacchi sferrati tramite armi (Spade, Asce, Tridenti)."
    );

    private WeaponProtectionEnchantment() {
    }
}

