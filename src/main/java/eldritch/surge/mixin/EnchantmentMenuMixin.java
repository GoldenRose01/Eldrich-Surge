package eldritch.surge.mixin;

import eldritch.surge.block.EldritchBlocks;
import eldritch.surge.config.EnchantmentCapsConfig;
import eldritch.surge.menu.AdvancedEnchantingMenuMarker;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {
    @Shadow
    @Final
    private ContainerLevelAccess access;

    @WrapOperation(
            method = "*",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z")
    )
    private boolean eldritchSurge$useAmethystInAdvancedTable(ItemStack stack, Object item, Operation<Boolean> original) {
        if ((Object) this instanceof AdvancedEnchantingMenuMarker && item == Items.LAPIS_LAZULI) {
            return original.call(stack, Items.AMETHYST_SHARD);
        }

        return original.call(stack, item);
    }

    @Inject(method = "getEnchantmentList", at = @At("RETURN"), cancellable = true)
    private void eldritchSurge$filterByTable(
            RegistryAccess registryAccess,
            ItemStack stack,
            int slot,
            int level,
            CallbackInfoReturnable<List<EnchantmentInstance>> cir
    ) {
        boolean advancedTable = access.evaluate(
                (world, pos) -> world.getBlockState(pos).is(EldritchBlocks.ADVANCED_ENCHANTING_TABLE),
                false
        );

        cir.setReturnValue(cir.getReturnValue().stream()
                .filter(instance -> instance.enchantment().unwrapKey()
                        .map(key -> isAllowed(key.identifier(), advancedTable, stack, slot, level))
                        .orElse(true))
                .toList());
    }

    private static boolean isAllowed(Identifier enchantmentId, boolean advancedTable, ItemStack stack, int slot, int level) {
        boolean tableAllowed = advancedTable
                ? EnchantmentCapsConfig.isAllowedInAdvancedTable(enchantmentId)
                : EnchantmentCapsConfig.isAllowedInNormalTable(enchantmentId);

        return tableAllowed
                && EnchantmentCapsConfig.isAllowedForItem(enchantmentId, stack)
                && EnchantmentCapsConfig.isCompatibleWithExistingEnchantments(enchantmentId, stack)
                && EnchantmentCapsConfig.passesRarity(enchantmentId, stack, slot, level);
    }
}
