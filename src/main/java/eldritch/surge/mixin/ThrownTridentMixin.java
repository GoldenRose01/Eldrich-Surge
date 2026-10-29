package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.SeaBreezeMechanics;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin {
    @ModifyArg(method = "onHitEntity", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;hurtOrSimulate(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), index = 1)
    private float eldritchSurge$neptuneThrownDamage(float damage) {
        ThrownTrident trident = (ThrownTrident) (Object) this;
        ItemStack weapon = trident.getWeaponItem();
        if (!(trident.level() instanceof ServerLevel level) || weapon == null) return damage;
        int rank = eldritch.surge.enchantment.mechanics.EnchantmentLevels.onItem(level, weapon, "neptunes_will");
        return damage + eldritch.surge.enchantment.ench.NeptunesWillEnchantment.bonusDamage(rank);
    }

    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void eldritchSurge$releaseWindOnEntityHit(EntityHitResult hit, CallbackInfo ci) {
        ThrownTrident trident = (ThrownTrident) (Object) this;
        if (trident.level() instanceof ServerLevel level) {
            SeaBreezeMechanics.releaseWindCharge(level, trident, trident.getWeaponItem(), hit.getLocation());
        }
    }

    @Inject(method = "hitBlockEnchantmentEffects", at = @At("TAIL"))
    private void eldritchSurge$releaseWindOnBlockHit(ServerLevel level, BlockHitResult hit, ItemStack weapon, CallbackInfo ci) {
        SeaBreezeMechanics.releaseWindCharge(level, (ThrownTrident) (Object) this, weapon, hit.getLocation());
    }
}
