package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.SurvivalEnchantmentMechanics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class FatiguePlayerMixin {
    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void eldrichSurge$slowMining(BlockState state, CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        cir.setReturnValue(cir.getReturnValueF() * SurvivalEnchantmentMechanics.actionSpeed(player, player.getMainHandItem()));
    }

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("RETURN"), cancellable = true)
    private void eldrichSurge$slowAttack(CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        cir.setReturnValue(cir.getReturnValueF() / SurvivalEnchantmentMechanics.actionSpeed(player, player.getMainHandItem()));
    }
}
