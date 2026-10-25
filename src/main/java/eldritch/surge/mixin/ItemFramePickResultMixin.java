package eldritch.surge.mixin;

import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public abstract class ItemFramePickResultMixin {
    @Inject(method = "getPickResult", at = @At("HEAD"), cancellable = true)
    private void eldrichSurge$copyDisplayedItem(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack displayed = ((ItemFrame) (Object) this).getItem();
        if (!displayed.isEmpty()) {
            cir.setReturnValue(displayed.copy());
        }
    }
}
