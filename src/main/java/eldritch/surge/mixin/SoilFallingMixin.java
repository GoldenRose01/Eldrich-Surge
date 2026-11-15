package eldritch.surge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public abstract class SoilFallingMixin {
    @WrapOperation(
            method = "fallOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FarmlandBlock;turnToBaseBlock(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V")
    )
    private void eldritchSurge$protectFarmlandOnFall(FarmlandBlock instance, Entity entity, BlockState state, Level level, BlockPos pos, Operation<Void> original) {
        if (eldritchSurge$hasSoilFalling(level, entity)) {
            return;
        }
        original.call(instance, entity, state, level, pos);
    }

    @Inject(method = "turnToBaseBlock", at = @At("HEAD"), cancellable = true)
    private void eldritchSurge$protectTilledSoil(Entity entity, BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        if (eldritchSurge$hasSoilFalling(level, entity)) {
            ci.cancel();
        }
    }

    @Unique
    private static boolean eldritchSurge$hasSoilFalling(Level level, Entity entity) {
        return entity instanceof Player player
                && level instanceof net.minecraft.server.level.ServerLevel serverLevel
                && EnchantmentLevels.onArmor(serverLevel, player, "soil_falling") > 0;
    }
}
