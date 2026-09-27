package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import eldritch.surge.enchantment.mechanics.VanguardChargeMechanics;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class VanguardChargeMixin {
    @Inject(method = "stabAttack", at = @At("TAIL"))
    private void eldritchSurge$activateChargedSpearAttack(net.minecraft.world.entity.EquipmentSlot slot,
                                                          net.minecraft.world.entity.Entity target, float charge,
                                                          boolean critical, boolean sweep, boolean knockback,
                                                          CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || !(target instanceof LivingEntity livingTarget)) return;

        Player player = (Player) (Object) this;
        if (!player.isUsingItem()) return;
        ItemStack spear = player.getUseItem();
        int level = EnchantmentLevels.onItem(player.level(), spear, "vanguard_charge");
        if (level > 0 && spear.is(ItemTags.SPEARS)
                && player instanceof ServerPlayer serverPlayer && player.level() instanceof ServerLevel) {
            VanguardChargeMechanics.activate(serverPlayer, livingTarget, level);
        }
    }
}
