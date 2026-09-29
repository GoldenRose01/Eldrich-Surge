package eldritch.surge.mixin;

import eldritch.surge.EldritchSurge;
import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import eldritch.surge.enchantment.mechanics.SpecialAnvilRecipes;
import eldritch.surge.enchantment.mechanics.MobCategoryDamageMechanics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilEnchantmentsMixin {
    @Shadow
    @Final
    protected ContainerLevelAccess access;

    @Shadow
    @Final
    private DataSlot cost;

    @Shadow
    @Final
    protected Container inputSlots;

    @Shadow
    @Final
    protected ResultContainer resultSlots;

    @Inject(method = "createResult", at = @At("TAIL"))
    private void eldritchSurge$applyCustomAnvilEnchantments(CallbackInfo ci) {
        ItemStack left = inputSlots.getItem(AnvilMenu.INPUT_SLOT);
        ItemStack right = inputSlots.getItem(AnvilMenu.ADDITIONAL_SLOT);
        if (SpecialAnvilRecipes.createOutput(access, inputSlots, resultSlots, cost)) {
            return;
        }
        if (isMaxUnbreakingBook(left) && isMaxUnbreakingBook(right)) {
            access.evaluate((level, pos) -> {
                var registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                var deathbreak = registry.get(ResourceKey.create(Registries.ENCHANTMENT, EldritchSurge.id("deathbreak")));
                if (deathbreak.isPresent()) {
                    resultSlots.setItem(AnvilMenu.RESULT_SLOT,
                            EnchantmentHelper.createBook(new EnchantmentInstance(deathbreak.get(), 1)));
                    cost.set(Math.max(1, cost.get()));
                }
                return true;
            }, false);
        }

        ItemStack output = resultSlots.getItem(AnvilMenu.RESULT_SLOT);
        if (output.isEmpty() || output.is(Items.ENCHANTED_BOOK)) {
            return;
        }

        int leftComponents = MobCategoryDamageMechanics.categoryComponentCount(left);
        int rightComponents = MobCategoryDamageMechanics.categoryComponentCount(right);
        int outputComponents = MobCategoryDamageMechanics.categoryComponentCount(output);
        if (rightComponents > 0 && outputComponents > 3 && outputComponents > leftComponents) {
            resultSlots.setItem(AnvilMenu.RESULT_SLOT, ItemStack.EMPTY);
            cost.set(0);
            return;
        }

        access.evaluate((level, pos) -> {
            int deathbreak = EnchantmentLevels.onItem(level, output, "deathbreak");
            if (deathbreak > 0) {
                output.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
                Identifier unbreaking = Identifier.withDefaultNamespace("unbreaking");
                EnchantmentHelper.updateEnchantments(output, enchantments -> enchantments.removeIf(holder ->
                        holder.unwrapKey().map(key -> key.identifier().equals(unbreaking)).orElse(false)));
            }

            ItemStack repairTarget = inputSlots.getItem(AnvilMenu.INPUT_SLOT);
            if (!repairTarget.isEmpty() && repairTarget.getDamageValue() > 0
                    && output.getDamageValue() < repairTarget.getDamageValue()) {
                int welding = EnchantmentLevels.onItem(level, repairTarget, "welding");
                int originalCost = cost.get();
                if (welding > 0 && originalCost > 0) {
                    cost.set(Math.max(1, (int) Math.ceil(originalCost / (4.0D * Math.min(welding, 5)))));
                }
            }
            return true;
        }, false);
    }

    private static boolean isMaxUnbreakingBook(ItemStack stack) {
        if (!stack.is(Items.ENCHANTED_BOOK)) {
            return false;
        }
        var enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        return enchantments.size() == 1 && enchantments.entrySet().stream().anyMatch(entry ->
                entry.getIntValue() == 3 && entry.getKey().unwrapKey()
                        .map(key -> key.identifier().equals(net.minecraft.resources.Identifier.withDefaultNamespace("unbreaking")))
                        .orElse(false));
    }
}
