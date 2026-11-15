package eldritch.surge.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAttackAccessor {
    @Accessor("attackStrengthTicker")
    int eldritchSurge$getAttackStrengthTicker();

    @Accessor("attackStrengthTicker")
    void eldritchSurge$setAttackStrengthTicker(int ticker);
}
