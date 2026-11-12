package eldritch.surge.mixin;

import eldritch.surge.game.EldritchGameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.PoisonMobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PoisonMobEffect.class)
public abstract class PoisonMobEffectMixin {
    @Inject(method = "applyEffectTick", at = @At("HEAD"))
    private void eldritchSurge$makePoisonLethal(ServerLevel level, LivingEntity entity, int amplifier, CallbackInfoReturnable<Boolean> cir) {
        if (EldritchGameRules.isLethalPoisonFor(level, entity) && entity.getHealth() <= 1.0F) {
            entity.hurtServer(level, entity.damageSources().magic(), 1.0F);
        }
    }
}
