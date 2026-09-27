package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(Player.class)
public abstract class SoulbondPlayerMixin {
    @Shadow @Final private Inventory inventory;

    @Unique
    private final Map<Integer, ItemStack> eldritchSurge$soulboundStacks = new HashMap<>();

    @Inject(method = "dropEquipment", at = @At("HEAD"))
    private void eldritchSurge$keepSoulboundItemsOnDeath(ServerLevel level, CallbackInfo ci) {
        eldritchSurge$soulboundStacks.clear();
        int forest = 0;
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            forest = Math.max(forest, EnchantmentLevels.onItem(level, inventory.getItem(slot), "curse_of_the_forest"));
        }
        if (forest > 0) {
            Player player = (Player) (Object) this;
            level.explode(player, player.getX(), player.getY(), player.getZ(), forest == 1 ? 3.0F : 6.0F,
                    false, net.minecraft.world.level.Level.ExplosionInteraction.NONE);
        }
        if (level.getGameRules().get(GameRules.KEEP_INVENTORY)) {
            return;
        }

        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty() && EnchantmentLevels.onItem(level, stack, "soulbond") > 0) {
                eldritchSurge$soulboundStacks.put(slot, inventory.removeItemNoUpdate(slot));
            }
        }
    }

    @Inject(method = "dropEquipment", at = @At("TAIL"))
    private void eldritchSurge$restoreSoulboundItems(ServerLevel level, CallbackInfo ci) {
        for (Map.Entry<Integer, ItemStack> entry : eldritchSurge$soulboundStacks.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue());
        }
        eldritchSurge$soulboundStacks.clear();
    }
}
