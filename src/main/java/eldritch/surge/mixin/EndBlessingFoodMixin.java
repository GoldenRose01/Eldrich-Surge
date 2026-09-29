package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class EndBlessingFoodMixin {
    @Shadow public abstract float getSaturationLevel();
    @Shadow public abstract void setSaturation(float saturation);

    @Unique private float eldritchSurge$savedSaturation;
    @Unique private boolean eldritchSurge$preserveSaturation;

    @Inject(method = "tick(Lnet/minecraft/server/level/ServerPlayer;)V", at = @At("HEAD"))
    private void eldritchSurge$saveEndSaturation(ServerPlayer player, CallbackInfo ci) {
        eldritchSurge$preserveSaturation = player.level().dimension() == Level.END
                && EnchantmentLevels.onArmor(player.level(), player, "end_blessing") > 0;
        if (eldritchSurge$preserveSaturation) {
            eldritchSurge$savedSaturation = getSaturationLevel();
        }
    }

    @Inject(method = "tick(Lnet/minecraft/server/level/ServerPlayer;)V", at = @At("TAIL"))
    private void eldritchSurge$restoreEndSaturation(ServerPlayer player, CallbackInfo ci) {
        if (eldritchSurge$preserveSaturation) {
            setSaturation(eldritchSurge$savedSaturation);
        }
        eldritchSurge$preserveSaturation = false;
    }
}
