package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class QuickHitMixin {
    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("RETURN"), cancellable = true)
    private void eldritchSurge$reduceAttackCooldown(CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        int level = EnchantmentLevels.onItem(player.level(), player.getMainHandItem(), "quick_hit");
        if (level > 0) {
            cir.setReturnValue(cir.getReturnValue() / (1.0F + 0.1F * level));
        }
    }
}
