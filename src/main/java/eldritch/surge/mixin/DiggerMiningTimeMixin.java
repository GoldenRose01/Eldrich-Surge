package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class DiggerMiningTimeMixin {
    @Unique
    private static final ThreadLocal<Boolean> eldrichSurge$calculatingArea = ThreadLocal.withInitial(() -> false);

    @Inject(method = "getDestroyProgress", at = @At("RETURN"), cancellable = true)
    private void eldrichSurge$includeAreaMiningTime(Player player, BlockGetter level, BlockPos pos,
                                                    CallbackInfoReturnable<Float> cir) {
        if (eldrichSurge$calculatingArea.get() || cir.getReturnValueF() <= 0.0F
                || EnchantmentLevels.onItem(player.level(), player.getMainHandItem(), "digger") <= 0) {
            return;
        }

        Direction.Axis normalAxis = Math.abs(player.getXRot()) > 60.0F
                ? Direction.Axis.Y
                : player.getDirection().getAxis();
        BlockState centerState = (BlockState) (Object) this;
        double totalTicks = 0.0D;

        eldrichSurge$calculatingArea.set(true);
        try {
            for (int first = -1; first <= 1; first++) {
                for (int second = -1; second <= 1; second++) {
                    BlockPos targetPos = first == 0 && second == 0
                            ? pos
                            : eldrichSurge$offset(pos, normalAxis, first, second);
                    BlockState targetState = first == 0 && second == 0
                            ? centerState
                            : level.getBlockState(targetPos);
                    if (targetState.isAir() || targetState.getDestroySpeed(level, targetPos) < 0.0F) {
                        continue;
                    }

                    float progressPerTick = targetState.getDestroyProgress(player, level, targetPos);
                    if (progressPerTick > 0.0F) {
                        // Each participating block costs 90% of its normal mining time.
                        totalTicks += 0.9D / progressPerTick;
                    }
                }
            }
        } finally {
            eldrichSurge$calculatingArea.set(false);
        }

        if (totalTicks > 0.0D) {
            cir.setReturnValue((float) (1.0D / totalTicks));
        }
    }

    @Unique
    private static BlockPos eldrichSurge$offset(BlockPos center, Direction.Axis normalAxis, int first, int second) {
        return switch (normalAxis) {
            case X -> center.offset(0, first, second);
            case Y -> center.offset(first, 0, second);
            case Z -> center.offset(first, second, 0);
        };
    }
}
