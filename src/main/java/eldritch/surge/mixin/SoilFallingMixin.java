package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public abstract class SoilFallingMixin {
    @Inject(method = "turnToBaseBlock", at = @At("HEAD"), cancellable = true)
    private void eldritchSurge$protectTilledSoil(Entity entity, BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        if (entity instanceof Player player
                && EnchantmentLevels.onItem(player.level(), player.getItemBySlot(EquipmentSlot.FEET), "soil_falling") > 0) {
            ci.cancel();
        }
    }
}
