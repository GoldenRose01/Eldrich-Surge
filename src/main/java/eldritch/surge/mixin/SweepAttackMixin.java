package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class SweepAttackMixin {
    @Inject(method = "isSweepAttack", at = @At("RETURN"), cancellable = true)
    private void eldritchSurge$allowHoeSweep(boolean strongAttack, boolean critical, boolean sprinting, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        ItemStack held = player.getMainHandItem();
        if (strongAttack && !critical && !sprinting && player.onGround()
                && held.is(ItemTags.HOES)
                && EnchantmentLevels.onItem(player.level(), held, "void_sweep") > 0) {
            cir.setReturnValue(true);
            return;
        }

        if (cir.getReturnValue() && eldritch.surge.game.EldritchGameRules.requiresSweepingEnchantment(player.level())) {
            int sweeping = EnchantmentLevels.onItem(player.level(), held, "dual_sweeping")
                    + EnchantmentLevels.onItem(player.level(), held, "void_sweep")
                    + EnchantmentLevels.onMinecraftItem(player.level(), held, "sweeping_edge");
            if (sweeping <= 0) {
                cir.setReturnValue(false);
            }
        }
    }

    @ModifyArg(
            method = "doSweepAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"),
            index = 2
    )
    private float eldritchSurge$modifySweepDamage(float amount) {
        Player player = (Player) (Object) this;
        ItemStack held = player.getMainHandItem();
        double dual = EnchantmentLevels.onItemStrength(player.level(), held, "dual_sweeping");
        if (dual > 0 && eldritch.surge.combat.DualWieldCombatMechanics.isDualWieldingSameWeaponType(player)) {
            return amount * 1.5F;
        }
        int voidSweep = EnchantmentLevels.onItem(player.level(), held, "void_sweep");
        if (voidSweep > 0 && held.is(ItemTags.HOES)) return 10.0F + voidSweep;
        return amount;
    }

    @Inject(method = "doSweepAttack", at = @At("HEAD"))
    private void eldritchSurge$showDualSweepParticles(
            net.minecraft.world.entity.Entity target,
            float baseDamage,
            net.minecraft.world.damagesource.DamageSource damageSource,
            float attackStrengthScale,
            CallbackInfo ci) {
        Player player = (Player) (Object) this;
        ItemStack mainHand = player.getMainHandItem();
        if (EnchantmentLevels.onItem(player.level(), mainHand, "dual_sweeping") > 0
                && eldritch.surge.combat.DualWieldCombatMechanics.isDualWieldingSameWeaponType(player)
                && player.level() instanceof ServerLevel serverLevel) {
            double yawRad = Math.toRadians(player.getYRot());
            double forwardX = -Math.sin(yawRad);
            double forwardZ = Math.cos(yawRad);
            double rightX = Math.cos(yawRad);
            double rightZ = Math.sin(yawRad);
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SWEEP_ATTACK,
                    player.getX() + forwardX * 1.1D + rightX * 0.65D, player.getY() + 1.0D,
                    player.getZ() + forwardZ * 1.1D + rightZ * 0.65D, 2, 0.2D, 0.1D, 0.2D, 0.0D);
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SWEEP_ATTACK,
                    player.getX() - forwardX * 1.1D - rightX * 0.65D, player.getY() + 1.0D,
                    player.getZ() - forwardZ * 1.1D - rightZ * 0.65D, 2, 0.2D, 0.1D, 0.2D, 0.0D);

            // Secondary sweep in the opposite direction
            float reverseSweepDamage = (1.0F + baseDamage) * 1.5F;
            net.minecraft.world.phys.Vec3 reverseCenter = player.position().add(-forwardX * 1.5D, 0.5D, -forwardZ * 1.5D);
            net.minecraft.world.phys.AABB reverseBox = new net.minecraft.world.phys.AABB(reverseCenter, reverseCenter).inflate(1.25D, 0.5D, 1.25D);
            eldritch.surge.combat.SecondaryEnchantmentDamage.run(() -> {
                for (net.minecraft.world.entity.LivingEntity other : serverLevel.getEntitiesOfClass(
                        net.minecraft.world.entity.LivingEntity.class, reverseBox)) {
                    if (other != player && other != target && !player.isAlliedTo(other)
                            && player.distanceToSqr(other) < 9.0D) {
                        other.knockback(0.4D, forwardX, forwardZ, damageSource, 0.0F);
                        other.hurtServer(serverLevel, damageSource, reverseSweepDamage);
                    }
                }
            });
        }
    }

    @ModifyArg(
            method = "doSweepAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDDLnet/minecraft/world/damagesource/DamageSource;F)V"),
            index = 4
    )
    private float eldritchSurge$increaseHoeSweepKnockback(float amount) {
        Player player = (Player) (Object) this;
        ItemStack held = player.getMainHandItem();
        return held.is(ItemTags.HOES) && EnchantmentLevels.onItem(player.level(), held, "void_sweep") > 0 ? 2.0F : amount;
    }

    @ModifyArg(
            method = "doSweepAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"),
            index = 0
    )
    private double eldrichSurge$extendHoeSweepX(double amount) {
        return eldritchSurge$extendHoeSweep(amount);
    }

    @ModifyArg(
            method = "doSweepAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"),
            index = 2
    )
    private double eldrichSurge$extendHoeSweepZ(double amount) {
        return eldritchSurge$extendHoeSweep(amount);
    }

    @ModifyConstant(method = "doSweepAttack", constant = @Constant(doubleValue = 9.0D))
    private double eldrichSurge$extendHoeSweepDistance(double amount) {
        Player player = (Player) (Object) this;
        ItemStack held = player.getMainHandItem();
        return held.is(ItemTags.HOES) && EnchantmentLevels.onItem(player.level(), held, "void_sweep") > 0
                ? 25.0D : amount;
    }

    private double eldritchSurge$extendHoeSweep(double amount) {
        Player player = (Player) (Object) this;
        ItemStack held = player.getMainHandItem();
        return held.is(ItemTags.HOES) && EnchantmentLevels.onItem(player.level(), held, "void_sweep") > 0
                ? amount + 2.0D : amount;
    }
}
