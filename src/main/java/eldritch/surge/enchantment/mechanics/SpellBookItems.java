package eldritch.surge.enchantment.mechanics;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.List;
import java.util.Optional;

public final class SpellBookItems {
    private SpellBookItems() {
    }

    public static void markIfSpellBook(ItemStack stack) {
        if (isSpellBook(stack)) {
            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(true), List.of(), List.of()));
        }
    }

    public static boolean isSpellBook(ItemStack stack) {
        return stack.is(Items.ENCHANTED_BOOK) && spellId(stack).isPresent();
    }

    public static Optional<String> spellId(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet().stream()
                .map(entry -> entry.getKey().unwrapKey().map(key -> key.identifier()).orElse(null))
                .filter(SpellBookItems::isCastSpell)
                .map(Identifier::getPath)
                .findFirst();
    }

    public static boolean isCastSpell(Holder<Enchantment> enchantment) {
        return enchantment.unwrapKey()
                .map(key -> isCastSpell(key.identifier()))
                .orElse(false);
    }

    public static boolean isCastSpell(Identifier id) {
        if (!id.getNamespace().equals("eldritch-surge")) {
            return false;
        }

        ModEnchantmentDefinition definition = ModEnchantmentDefinitions.BY_ID.get(id.getPath());
        return definition != null && definition.isCastSpell();
    }
}
