package eldritch.surge.mixin;

import eldritch.surge.combat.AdditionalDamageCalculator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {
    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float eldritchSurge$applyBladeOfApocalypse(float amount, ServerLevel world, DamageSource source) {
        Entity attacker = source.getEntity();
        if (attacker == null) {
            return amount;
        }

        return AdditionalDamageCalculator.addBladeOfApocalypseDamage(
                world,
                amount,
                (LivingEntity) (Object) this,
                attacker
        );
    }
}
