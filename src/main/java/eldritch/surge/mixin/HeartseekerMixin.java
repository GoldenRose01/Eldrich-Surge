package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntity.class)
public abstract class HeartseekerMixin {
    @ModifyArgs(
            method = "getDamageAfterArmorAbsorb",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F")
    )
    private void eldritchSurge$critOnBackstabAndPierceArmor(Args args) {
        if (!(args.get(0) instanceof LivingEntity victim)
                || !(args.get(2) instanceof DamageSource source)
                || !(source.getEntity() instanceof LivingEntity attacker)
                || !(attacker.level() instanceof ServerLevel level)) return;
        int heartseeker = EnchantmentLevels.onItem(level, attacker.getMainHandItem(), "heartseeker");
        if (heartseeker <= 0) return;

        Vec3 towardAttacker = attacker.position().subtract(victim.position()).normalize();
        if (victim.getViewVector(1.0F).dot(towardAttacker) < -0.2D) {
            args.set(1, ((Float) args.get(1)) * 1.5F);
            args.set(3, ((Float) args.get(3)) * 0.5F);
        }
    }
}
