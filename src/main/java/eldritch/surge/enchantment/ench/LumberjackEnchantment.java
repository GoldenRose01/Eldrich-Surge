package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class LumberjackEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "lumberjack",
            "Lumberjack",
            "TOOLS (AXES)",
            1,
            "Rare",
            "Normal, Advanced",
            List.of("#eldrich-surge:enchantable/lumberjack"),
            List.of("hand"),
            "",
            "Abbatti l'intero albero in un solo colpo mentre sei abbassato con un'ascia idonea."
    );

    private LumberjackEnchantment() {
    }
}
