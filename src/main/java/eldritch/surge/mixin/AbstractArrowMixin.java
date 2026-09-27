package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.ProjectileEnchantmentMechanics;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
    @Shadow
    private double baseDamage;

    @Unique
    private double eldritchSurge$damageBeforeHit;

    @Unique
    private boolean eldritchSurge$modifiedDamage;

    @Unique
    private boolean eldritchSurge$popDetonated;

    @ModifyArg(
            method = "shoot",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;shoot(DDDFF)V"),
            index = 3
    )
    private float eldritchSurge$increaseElasticityVelocity(float velocity) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        return arrow.level() instanceof ServerLevel level
                ? ProjectileEnchantmentMechanics.scaleArrowVelocity(level, arrow, velocity)
                : velocity;
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void eldritchSurge$applyArrowHitBonuses(EntityHitResult hit, CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        if (!(arrow.level() instanceof ServerLevel level)) {
            return;
        }

        eldritchSurge$damageBeforeHit = baseDamage;
        double sniperBonus = ProjectileEnchantmentMechanics.sniperBonus(level, arrow);
        boolean instantKill = ProjectileEnchantmentMechanics.isNinelevenTarget(level, arrow, hit.getEntity());
        if (sniperBonus > 0.0D || instantKill) {
            eldritchSurge$modifiedDamage = true;
            baseDamage += sniperBonus;
            if (instantKill) {
                baseDamage = Math.max(baseDamage, 1_000_000.0D);
            }
        }
    }

    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void eldritchSurge$applyHitEffects(EntityHitResult hit, CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        if (!(arrow.level() instanceof ServerLevel level)) {
            return;
        }

        ProjectileEnchantmentMechanics.applyCurseOfTarget(level, arrow, hit.getEntity());
        ProjectileEnchantmentMechanics.applyCurseOfSpider(level, arrow, hit.getEntity());
        eldritchSurge$detonatePop(level, arrow, hit.getLocation().x, hit.getLocation().y, hit.getLocation().z);
        eldritchSurge$restoreBaseDamage();
    }

    @Inject(method = "onHitBlock", at = @At("TAIL"))
    private void eldritchSurge$detonatePopOnBlock(BlockHitResult hit, CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        if (arrow.level() instanceof ServerLevel level) {
            eldritchSurge$detonatePop(level, arrow, hit.getLocation().x, hit.getLocation().y, hit.getLocation().z);
        }
    }

    @Unique
    private void eldritchSurge$detonatePop(ServerLevel level, AbstractArrow arrow, double x, double y, double z) {
        if (!eldritchSurge$popDetonated
                && ProjectileEnchantmentMechanics.popLevel(level, arrow) > 0) {
            eldritchSurge$popDetonated = true;
            ProjectileEnchantmentMechanics.detonatePop(level, arrow, x, y, z);
        }
    }

    @Unique
    private void eldritchSurge$restoreBaseDamage() {
        if (eldritchSurge$modifiedDamage) {
            baseDamage = eldritchSurge$damageBeforeHit;
            eldritchSurge$modifiedDamage = false;
        }
    }
}
