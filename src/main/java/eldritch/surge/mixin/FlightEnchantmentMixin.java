package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.SurvivalEnchantmentMath;
import eldritch.surge.enchantment.mechanics.SurvivalEnchantmentMechanics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class FlightEnchantmentMixin {
    @ModifyArg(method = "travelFallFlying", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"), index = 1)
    private Vec3 eldrichSurge$boostGliding(Vec3 movement) {
        LivingEntity entity = (LivingEntity) (Object) this;
        int rank = SurvivalEnchantmentMechanics.elytraLevel(entity, "happy_boost");
        // Scale this tick's displacement, not stored velocity: no exponential acceleration.
        // Runs on both sides to preserve player movement prediction.
        return movement.scale(SurvivalEnchantmentMath.flightSpeed(rank));
    }
}
