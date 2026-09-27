package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class SnowshoeingFreezeMixin {
    @Inject(method = "canFreeze", at = @At("RETURN"), cancellable = true)
    private void eldritchSurge$preventPowderSnowFreezing(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (cir.getReturnValue() && entity instanceof Player player && player.isInPowderSnow
                && EnchantmentLevels.onItem(player.level(), player.getItemBySlot(EquipmentSlot.FEET), "snowshoeing") > 0) {
            cir.setReturnValue(false);
        }
    }
}
