package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentBookTags;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class EnchantmentBookTagMixin {
    @Inject(method = "enchant(Lnet/minecraft/core/Holder;I)V", at = @At("TAIL"))
    private void eldritchSurge$updateBookCompatibilityTag(Holder<Enchantment> enchantment, int level, CallbackInfo ci) {
        EnchantmentBookTags.updateBookModelTag((ItemStack) (Object) this);
    }

    @Inject(method = "set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", at = @At("TAIL"))
    private void eldritchSurge$refreshBookTagOnEnchantments(DataComponentType<?> component, Object value,
                                                            CallbackInfoReturnable<Object> cir) {
        if (component == DataComponents.ENCHANTMENTS || component == DataComponents.STORED_ENCHANTMENTS) {
            EnchantmentBookTags.updateBookModelTag((ItemStack) (Object) this);
        }
    }

    @Inject(method = "remove(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;", at = @At("TAIL"))
    private void eldritchSurge$clearOrRefreshBookTagOnEnchantmentRemoval(DataComponentType<?> component,
                                                                         CallbackInfoReturnable<Object> cir) {
        if (component == DataComponents.ENCHANTMENTS || component == DataComponents.STORED_ENCHANTMENTS) {
            EnchantmentBookTags.updateBookModelTag((ItemStack) (Object) this);
        }
    }
}
