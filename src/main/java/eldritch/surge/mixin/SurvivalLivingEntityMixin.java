package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.SurvivalEnchantmentMechanics;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SwingAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class SurvivalLivingEntityMixin {
    @Shadow protected float lastHurt;
    @Unique private double eldrichSurge$useProgress;
    @Unique private net.minecraft.world.InteractionHand eldrichSurge$swingHand = net.minecraft.world.InteractionHand.MAIN_HAND;

    @Inject(method = "hurtServer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V"), cancellable = true)
    private void eldrichSurge$dodge(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity victim = (LivingEntity) (Object) this;
        // Roll only for hits that passed vanilla's immunity/cooldown checks. A dodged
        // contact hit must consume the cooldown too, or hazards retry every tick.
        if (damage > 0 && SurvivalEnchantmentMechanics.dodge(level, victim)) {
            lastHurt = damage;
            victim.damageCooldownTime = 20;
            if (victim instanceof net.minecraft.server.level.ServerPlayer player) {
                player.sendSystemMessage(net.minecraft.network.chat.Component.translatable("message.eldrich-surge.dodge")
                        .withStyle(net.minecraft.ChatFormatting.AQUA), true);
            }
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void eldrichSurge$immortality(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (SurvivalEnchantmentMechanics.saveFromDeath((LivingEntity) (Object) this, source)) cir.setReturnValue(true);
    }

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float eldrichSurge$reduceFlightImpact(float damage, ServerLevel level, DamageSource source) {
        if (!source.is(net.minecraft.world.damagesource.DamageTypes.FLY_INTO_WALL)) return damage;
        int rank = SurvivalEnchantmentMechanics.elytraLevel((LivingEntity) (Object) this, "kinetic_protection");
        return damage * eldritch.surge.enchantment.mechanics.SurvivalEnchantmentMath.kineticImpactMultiplier(rank);
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void eldrichSurge$fear(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity victim = (LivingEntity) (Object) this;
        if (!cir.getReturnValueZ() || !victim.isAlive()
                || !(victim instanceof eldritch.surge.enchantment.mechanics.FearDelayTarget creeper)
                || !(source.getEntity() instanceof net.minecraft.world.entity.player.Player player)
                || source.getDirectEntity() != player) return;
        ItemStack sword = player.getMainHandItem();
        if (sword.is(net.minecraft.tags.ItemTags.SWORDS)
                && eldritch.surge.enchantment.mechanics.EnchantmentLevels.onItem(level, sword, "fear") > 0) {
            creeper.eldrichSurge$delayExplosion();
        }
    }

    @Inject(method = "startUsingItem", at = @At("HEAD"))
    private void eldrichSurge$resetDraw(net.minecraft.world.InteractionHand hand, CallbackInfo ci) {
        eldrichSurge$useProgress = 0.0D;
    }

    @Inject(method = "updateUsingItem", at = @At("HEAD"), cancellable = true)
    private void eldrichSurge$slowDraw(ItemStack stack, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        float speed = SurvivalEnchantmentMechanics.actionSpeed(entity, stack);
        if (speed >= 1.0F) return;
        eldrichSurge$useProgress += speed;
        if (eldrichSurge$useProgress < 1.0D) ci.cancel();
        else eldrichSurge$useProgress -= 1.0D;
    }

    @Inject(method = "getModifiedSwingDuration", at = @At("RETURN"), cancellable = true)
    private void eldrichSurge$slowSwing(SwingAnimation animation, CallbackInfoReturnable<Integer> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        cir.setReturnValue((int) Math.ceil(cir.getReturnValueI()
                / SurvivalEnchantmentMechanics.actionSpeed(entity, entity.getItemInHand(eldrichSurge$swingHand))));
    }

    @Inject(method = "swing", at = @At("HEAD"))
    private void eldrichSurge$rememberSwingHand(net.minecraft.world.InteractionHand hand, SwingAnimation animation,
                                               boolean broadcast, CallbackInfoReturnable<Boolean> cir) {
        eldrichSurge$swingHand = hand;
    }
}
