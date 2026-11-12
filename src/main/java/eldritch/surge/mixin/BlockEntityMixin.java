package eldritch.surge.mixin;

import eldritch.surge.menu.ReagentHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {
    @Shadow
    protected Level level;

    @Inject(method = "preRemoveSideEffects", at = @At("HEAD"))
    private void eldritchSurge$dropReagentOnBreak(BlockPos pos, BlockState state, CallbackInfo ci) {
        if ((Object) this instanceof ReagentHolder holder) {
            ItemStack reagent = holder.eldritchSurge$getReagent();
            if (reagent != null && !reagent.isEmpty()) {
                if (this.level != null && !this.level.isClientSide()) {
                    Containers.dropItemStack(this.level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, reagent);
                    holder.eldritchSurge$setReagent(ItemStack.EMPTY);
                }
            }
        }
    }
}
