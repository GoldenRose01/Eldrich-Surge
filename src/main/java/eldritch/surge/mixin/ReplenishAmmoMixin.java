package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.ProjectileEnchantmentMechanics;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileWeaponItem.class)
public abstract class ReplenishAmmoMixin {
    @Unique
    private static final ThreadLocal<Integer> eldritchSurge$ammoCount = ThreadLocal.withInitial(() -> 0);

    @Inject(method = "useAmmo", at = @At("HEAD"))
    private static void eldritchSurge$captureAmmoCount(ItemStack weapon, ItemStack ammo, LivingEntity shooter,
                                                       boolean creative, CallbackInfo ci) {
        eldritchSurge$ammoCount.set(ammo.getCount());
    }

    @Inject(method = "useAmmo", at = @At("RETURN"))
    private static void eldritchSurge$refundAmmo(ItemStack weapon, ItemStack ammo, LivingEntity shooter,
                                                  boolean creative, CallbackInfoReturnable<ItemStack> cir) {
        int originalCount = eldritchSurge$ammoCount.get();
        eldritchSurge$ammoCount.remove();
        if (ammo.getCount() >= originalCount || creative || !(shooter.level() instanceof ServerLevel level)) {
            return;
        }
        ProjectileEnchantmentMechanics.tryReplenish(level, weapon, cir.getReturnValue(), shooter);
    }
}
