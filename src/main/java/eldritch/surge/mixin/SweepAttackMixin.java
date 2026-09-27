package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class SweepAttackMixin {
    @Inject(method = "isSweepAttack", at = @At("RETURN"), cancellable = true)
    private void eldritchSurge$allowHoeSweep(boolean critical, boolean sprinting, boolean strongAttack, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        ItemStack held = player.getMainHandItem();
        if (!critical && !sprinting && !strongAttack && player.onGround()
                && held.is(ItemTags.HOES)
                && EnchantmentLevels.onItem(player.level(), held, "void_sweep") > 0) {
            cir.setReturnValue(true);
        }
    }

    @ModifyArg(
            method = "doSweepAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getEnchantedDamage(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;)F"),
            index = 1
    )
    private float eldritchSurge$modifySweepDamage(float amount) {
        Player player = (Player) (Object) this;
        ItemStack held = player.getMainHandItem();
        int dual = EnchantmentLevels.onItem(player.level(), held, "dual_sweeping");
        if (dual > 0 && player.getOffhandItem().is(ItemTags.SWORDS)) return amount * 2.0F;
        int voidSweep = EnchantmentLevels.onItem(player.level(), held, "void_sweep");
        if (voidSweep > 0 && held.is(ItemTags.HOES)) return 10.0F + voidSweep;
        return amount;
    }

    @Inject(method = "doSweepAttack", at = @At("HEAD"))
    private void eldritchSurge$showDualSweepParticles(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        ItemStack mainHand = player.getMainHandItem();
        if (EnchantmentLevels.onItem(player.level(), mainHand, "dual_sweeping") > 0
                && player.getOffhandItem().is(ItemTags.SWORDS)
                && player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SWEEP_ATTACK,
                    player.getX(), player.getY() + 1.0D, player.getZ(), 6,
                    0.45D, 0.25D, 0.45D, 0.0D);
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
}
