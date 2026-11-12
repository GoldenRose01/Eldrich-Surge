package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import eldritch.surge.enchantment.mechanics.SpiritCastMechanics;
import eldritch.surge.game.LoyaltyTridentStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerRespawnMixin {
    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void eldritchSurge$restoreLoyaltyTridentsOnRespawn(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        if (!alive && oldPlayer != null) {
            SpiritCastMechanics.resetKillCount(oldPlayer.getUUID());
            ServerPlayer newPlayer = (ServerPlayer) (Object) this;
            if (!newPlayer.level().getGameRules().get(GameRules.KEEP_INVENTORY)) {
                Inventory oldInventory = oldPlayer.getInventory();
                Inventory newInventory = newPlayer.getInventory();
                for (int slot = 0; slot < oldInventory.getContainerSize(); slot++) {
                    ItemStack stack = oldInventory.getItem(slot);
                    if (!stack.isEmpty() && EnchantmentLevels.onItem(newPlayer.level(), stack, "soulbond") > 0) {
                        newInventory.setItem(slot, stack.copy());
                    }
                }
            }
        }
        LoyaltyTridentStorage.restoreTridents((ServerPlayer) (Object) this);
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void eldritchSurge$resetSpiritCastOnDeath(DamageSource damageSource, CallbackInfo ci) {
        SpiritCastMechanics.resetKillCount(((ServerPlayer) (Object) this).getUUID());
    }
}
