package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import eldritch.surge.enchantment.mechanics.PassiveEnchantmentMechanics;
import eldritch.surge.enchantment.mechanics.SpellCastMechanics;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Unique
    private boolean eldritchSurge$hasLeftGround;

    @Inject(method = "tick", at = @At("HEAD"))
    private void eldritchSurge$applyEnduring(CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) (Object) this;
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }

        if (EnchantmentLevels.onItem(level, entity.getItem(), "enduring") > 0) {
            entity.setUnlimitedLifetime();
        }

        if (entity.onGround()) {
            if (eldritchSurge$hasLeftGround) {
                SpellCastMechanics.castFromDroppedBook(level, entity);
            }
        } else {
            eldritchSurge$hasLeftGround = true;
        }
    }

    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void eldritchSurge$siphonIntoShulker(Player player, CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) (Object) this;
        if (entity.level() instanceof ServerLevel level
                && player instanceof ServerPlayer serverPlayer
                && PassiveEnchantmentMechanics.trySiphonPickup(level, serverPlayer, entity)) {
            ci.cancel();
        }
    }
}
