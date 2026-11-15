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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilEnchantmentsMixin extends ItemCombinerMenu {

    @Shadow
    @Final
    private DataSlot cost;

    @Unique
    private static final Identifier WELDING_ID = EldritchSurge.id("welding");

    @Unique
    private static final String WELDING_FUSIONS_KEY = "eldrich_welding_fusions";

    protected AnvilEnchantmentsMixin(MenuType<?> type, int containerId, Inventory inventory,
                                     ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slots) {
        super(type, containerId, inventory, access, slots);
    }

    @ModifyConstant(
            method = "createResult",
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/core/component/DataComponents;CUSTOM_NAME:Lnet/minecraft/core/component/DataComponentType;")),
            constant = @Constant(intValue = 40)
    )
    private int eldritchSurge$adjustAnvilCostCap(int original) {
        ItemStack left = inputSlots.getItem(AnvilMenu.INPUT_SLOT);
        ItemStack right = inputSlots.getItem(AnvilMenu.ADDITIONAL_SLOT);
        return (hasWelding(left) || hasWelding(right)) ? 50 : Integer.MAX_VALUE;
    }

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
            boolean hadWelding = EnchantmentLevels.onItem(level, repairTarget, "welding") > 0;
            double welding = EnchantmentLevels.onItemStrength(level, output, "welding");
            if (welding > 0) {
                if (!hadWelding) {
                    output.set(DataComponents.REPAIR_COST, 0);
                    CustomData.update(DataComponents.CUSTOM_DATA, output, tag -> tag.putInt(WELDING_FUSIONS_KEY, 0));
                } else {
                    int prevFusions = repairTarget.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                            .copyTag().getIntOr(WELDING_FUSIONS_KEY, Math.max(0, repairTarget.getOrDefault(DataComponents.REPAIR_COST, 0)));
                    int fusions = prevFusions + 1;
                    CustomData.update(DataComponents.CUSTOM_DATA, output, tag -> tag.putInt(WELDING_FUSIONS_KEY, fusions));
                    int logRepairCost = Math.min(49, Math.max(1,
                            (int) Math.round((3.5D / (0.6D + 0.4D * welding)) * Math.log(fusions + 1.0D))));
                    output.set(DataComponents.REPAIR_COST, logRepairCost);
                }

                if (!repairTarget.isEmpty() && repairTarget.getDamageValue() > 0
                        && output.getDamageValue() < repairTarget.getDamageValue()) {
                    int originalCost = cost.get();
                    if (originalCost > 0) {
                        cost.set(Math.max(1, Math.min(49, (int) Math.ceil(originalCost / (4.0D * welding)))));
                    }
                }
            }
            return true;
        }, false);
    }

    private static boolean hasWelding(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet().stream()
                .anyMatch(entry -> entry.getIntValue() > 0 && entry.getKey().unwrapKey()
                        .map(key -> key.identifier().equals(WELDING_ID))
                        .orElse(false));
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
