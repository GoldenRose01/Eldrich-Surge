package eldritch.surge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import eldritch.surge.enchantment.mechanics.MagneticArmorStand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorStandItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorStandItem.class)
public abstract class ArmorStandItemMixin {
    @WrapOperation(
            method = "useOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V")
    )
    private void eldritchSurge$applyMagneticOnPlace(ServerLevel level, Entity entity, Operation<Void> original, UseOnContext context) {
        if (entity instanceof ArmorStand armorStand && armorStand instanceof MagneticArmorStand magnetic) {
            int magneticLevel = EnchantmentLevels.onItem(level, context.getItemInHand(), "magnetic");
            if (magneticLevel > 0) {
                magnetic.eldritchSurge$setMagneticLevel(magneticLevel);
            }
        }
        original.call(level, entity);
    }
}
