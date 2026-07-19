package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import eldritch.surge.enchantment.mechanics.SpellCastMechanics;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void eldritchSurge$applyEnduring(CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) (Object) this;
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }

        if (EnchantmentLevels.onItem(level, entity.getItem(), "enduring") > 0) {
            entity.setUnlimitedLifetime();
        }

        SpellCastMechanics.castFromDroppedBook(level, entity);
    }
}
