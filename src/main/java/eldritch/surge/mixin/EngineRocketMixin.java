package eldritch.surge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.FireworkRocketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireworkRocketItem.class)
public abstract class EngineRocketMixin {
    @WrapOperation(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V")
    )
    private void eldritchSurge$returnElytraBoostRocket(ItemStack stack, int count, LivingEntity user, Operation<Void> original) {
        if (user instanceof Player player && player.isFallFlying() && player.level() instanceof ServerLevel level) {
            int engine = EnchantmentLevels.onItem(level, player.getItemBySlot(EquipmentSlot.CHEST), "engine");
            if (engine > 0 && level.getRandom().nextInt(100) < Math.min(100, engine * 10)) {
                return;
            }
        }
        original.call(stack, count, user);
    }
}
