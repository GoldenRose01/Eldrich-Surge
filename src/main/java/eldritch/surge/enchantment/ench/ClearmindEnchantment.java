package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class ClearmindEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "clearmind",
            "Clearmind",
            "WEAPONS (SWORDS, AXES, TRIDENTS)",
            5,
            "Ultra",
            "Advanced",
            List.of("#minecraft:enchantable/crossbow", "#minecraft:enchantable/bow"),
            List.of("hand"),
            "",
            "Incrementa lo zoom visivo se il giocatore rimane immobile per almeno 10 secondi tenendo l'arma tesa."
    );

    private ClearmindEnchantment() {
    }
}

