package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AccuracyShotArrowMixin {
    @Unique private long eldrichSurge$accuracyExpiry = -1L;

    @Unique
    private boolean eldrichSurge$isAccuracyShot() {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        ItemStack bow = arrow.getWeaponItem();
        // Use the weapon saved on the arrow, never the owner's current held item.
        return bow != null && bow.is(Items.BOW) && EnchantmentLevels.onItem(arrow.level(), bow, "accuracy_shot") > 0;
    }

    @ModifyArg(method = "shoot", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/Projectile;shoot(DDDFF)V"), index = 4)
    private float eldrichSurge$removeSpread(float spread) {
        return eldrichSurge$isAccuracyShot() ? 0.0F : spread;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void eldrichSurge$straightArrow(CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        if (arrow.level().isClientSide() || !eldrichSurge$isAccuracyShot()) return;
        arrow.setNoGravity(true);
        if (eldrichSurge$accuracyExpiry < 0) eldrichSurge$accuracyExpiry = arrow.level().getGameTime() + 600L;
        if (arrow.level().getGameTime() >= eldrichSurge$accuracyExpiry) {
            arrow.discard();
            ci.cancel();
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void eldrichSurge$saveAccuracyExpiry(ValueOutput output, CallbackInfo ci) {
        if (eldrichSurge$accuracyExpiry >= 0) output.putLong("eldrich_surge_accuracy_expiry", eldrichSurge$accuracyExpiry);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void eldrichSurge$loadAccuracyExpiry(ValueInput input, CallbackInfo ci) {
        eldrichSurge$accuracyExpiry = input.getLongOr("eldrich_surge_accuracy_expiry", -1L);
    }
}
