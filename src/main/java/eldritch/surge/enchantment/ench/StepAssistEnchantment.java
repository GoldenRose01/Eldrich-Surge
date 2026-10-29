package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import java.util.List;

public final class StepAssistEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "step_assist", "Step Assist", "BOOTS", 3, "Rare", "Normal Advanced",
            List.of("#minecraft:enchantable/foot_armor"), List.of("feet"), "", "Sale automaticamente fino a 1 blocco. I dislivelli superiori richiedono un salto; al livello III il salto supera 1,5 blocchi.");

    private StepAssistEnchantment() {}
}
