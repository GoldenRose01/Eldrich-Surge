package eldritch.surge.mixin;

import eldritch.surge.combat.AdditionalDamageCalculator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {
    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float eldritchSurge$applyBladeOfApocalypse(float amount, ServerWorld world, DamageSource source) {
        Entity attacker = source.getAttacker();
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
