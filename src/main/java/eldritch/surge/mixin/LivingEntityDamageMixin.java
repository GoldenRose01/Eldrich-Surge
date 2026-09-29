package eldritch.surge.mixin;

import eldritch.surge.combat.AdditionalDamageCalculator;
import eldritch.surge.enchantment.mechanics.DefensiveEnchantmentMechanics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {
    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float eldritchSurge$applyBladeOfApocalypse(float amount, ServerLevel world, DamageSource source) {
        LivingEntity victim = (LivingEntity) (Object) this;
        if (DefensiveEnchantmentMechanics.tryEscapeVoid(world, victim, source)) {
            return 0.0F;
        }

        Entity attacker = source.getEntity();
        if (attacker == null) {
            return amount;
        }

        return AdditionalDamageCalculator.addBladeOfApocalypseDamage(
                world,
                amount,
                victim,
                attacker,
                source
        );
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void eldritchSurge$afterSuccessfulHit(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        Entity attacker = source.getEntity();
        if (attacker == null) {
            return;
        }

        LivingEntity victim = (LivingEntity) (Object) this;
        DefensiveEnchantmentMechanics.retaliate(world, victim, attacker, source);
        AdditionalDamageCalculator.afterSuccessfulHit(world, victim, attacker, amount, source);
    }
}
