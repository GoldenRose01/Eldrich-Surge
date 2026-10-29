package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.SurvivalEnchantmentMechanics;
import net.minecraft.world.level.ServerExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerExplosion.class)
public abstract class FuseCutterExplosionMixin {
    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void eldrichSurge$cancelExplosion(CallbackInfoReturnable<Integer> cir) {
        if (SurvivalEnchantmentMechanics.cancelExplosion((ServerExplosion) (Object) this)) cir.setReturnValue(0);
    }
}
