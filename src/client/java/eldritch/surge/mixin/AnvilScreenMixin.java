package eldritch.surge.mixin;

import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {
    @Unique
    private static final Identifier WELDING_ID = Identifier.fromNamespaceAndPath("eldrich-surge", "welding");

    protected AnvilScreenMixin(AnvilMenu menu, Inventory inventory, Component title, Identifier texture) {
        super(menu, inventory, title, texture);
    }

    @ModifyConstant(method = "extractLabels", constant = @Constant(intValue = 40))
    private int eldritchSurge$replaceTooExpensiveLimit(int original) {
        for (int slot = 0; slot <= 2; slot++) {
            if (hasWelding(this.menu.getSlot(slot).getItem())) {
                return 50;
            }
        }
        return Integer.MAX_VALUE;
    }

    @Unique
    private static boolean hasWelding(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet().stream()
                .anyMatch(entry -> entry.getIntValue() > 0 && entry.getKey().unwrapKey()
                        .map(key -> key.identifier().equals(WELDING_ID))
                        .orElse(false));
    }
}
