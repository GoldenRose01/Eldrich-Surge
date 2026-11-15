package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class HeartseekerMixin {
    @Unique
    private float eldritchSurge$preStabHealth;

    @Inject(method = "stabAttack", at = @At("HEAD"))
    private void eldritchSurge$captureTargetHealth(EquipmentSlot slot, Entity target, float charge,
                                                   boolean critical, boolean sweep, boolean knockback,
                                                   CallbackInfoReturnable<Boolean> cir) {
        eldritchSurge$preStabHealth = 0.0F;
        Player player = (Player) (Object) this;
        if (!player.isUsingItem() || !(target instanceof LivingEntity livingTarget) || !(player.level() instanceof ServerLevel level)) {
            return;
        }
        ItemStack spear = player.getUseItem();
        if (spear.is(ItemTags.SPEARS) && EnchantmentLevels.onItem(level, spear, "heartseeker") > 0) {
            eldritchSurge$preStabHealth = livingTarget.getHealth();
        }
    }

    @Inject(method = "stabAttack", at = @At("RETURN"))
    private void eldritchSurge$stealStabHealth(EquipmentSlot slot, Entity target, float charge,
                                               boolean critical, boolean sweep, boolean knockback,
                                               CallbackInfoReturnable<Boolean> cir) {
        float before = eldritchSurge$preStabHealth;
        eldritchSurge$preStabHealth = 0.0F;
        if (before <= 0.0F || !cir.getReturnValue() || !(target instanceof LivingEntity livingTarget)) {
            return;
        }
        Player player = (Player) (Object) this;
        if (player.level() instanceof ServerLevel) {
            float stolen = Math.max(0.0F, before - livingTarget.getHealth());
            if (stolen > 0.0F) {
                player.heal(stolen);
            }
        }
    }
}
