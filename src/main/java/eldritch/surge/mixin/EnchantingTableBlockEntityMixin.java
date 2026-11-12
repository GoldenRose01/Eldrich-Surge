package eldritch.surge.mixin;

import eldritch.surge.menu.ReagentHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantingTableBlockEntity.class)
public abstract class EnchantingTableBlockEntityMixin extends BlockEntity implements ReagentHolder {
    @Unique
    private ItemStack eldritchSurge$reagent = ItemStack.EMPTY;

    protected EnchantingTableBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public ItemStack eldritchSurge$getReagent() {
        return eldritchSurge$reagent;
    }

    @Override
    public void eldritchSurge$setReagent(ItemStack stack) {
        this.eldritchSurge$reagent = stack == null ? ItemStack.EMPTY : stack.copy();
        setChanged();
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void eldritchSurge$saveReagent(ValueOutput output, CallbackInfo ci) {
        if (!eldritchSurge$reagent.isEmpty()) {
            output.store("eldritchSurge$reagent", ItemStack.CODEC, eldritchSurge$reagent);
        }
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void eldritchSurge$loadReagent(ValueInput input, CallbackInfo ci) {
        eldritchSurge$reagent = input.read("eldritchSurge$reagent", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }
}
