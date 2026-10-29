package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.FearDelayTarget;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Creeper.class)
public abstract class FearCreeperMixin implements FearDelayTarget {
    @Shadow private int maxSwell;

    @Override
    public void eldrichSurge$delayExplosion() {
        // Vanilla persists the fuse as a short; do not overflow it after repeated hits.
        maxSwell = (int) Math.min(Short.MAX_VALUE, (long) maxSwell + 20L);
    }
}
